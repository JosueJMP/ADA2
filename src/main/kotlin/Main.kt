package org.example

import java.io.File

data class Alumno(
    val apellido: String,
    val parcial1: Double,
    val parcial2: Double,
    val practicas: Double,
    val asistencia: Double,
    val notaFinal: Double? = null
)


// Función para leer calificaciones desde un archivo CSV
fun leerCalificaciones(rutaArchivo: String): List<Alumno> {
    val listaAlumnos = mutableListOf<Alumno>()


    // Lee el archivo CSV línea por línea
    File(rutaArchivo).useLines { lines ->
        val lineas = lines.toList()
        val encabezados = lineas.first().split(";")


        // Procesa las líneas a partir de la segunda
        lineas.drop(1).forEach { linea ->
            val valores = linea.split(";")

                // Crea un objeto Alumno usando los datos de la línea
                // Convierte las comas a puntos en las notas antes de convertirlas a Double
                val alumno = Alumno(
                    apellido = valores[0], // Apellido
                    parcial1 = valores[3].replace(',', '.').toDouble(),
                    parcial2 = valores[4].replace(',', '.').toDouble(),
                    practicas = valores[7].replace(',', '.').toDouble(),
                    asistencia = valores[2].toDouble()
                )
                listaAlumnos.add(alumno)
        }
    }

    // Ordena la lista por apellidos y la devuelve
    return listaAlumnos.sortedBy { it.apellido }
}


// Función para calcular las notas finales de los alumnos

fun calcularNotasFinales(listaAlumnos: List<Alumno>): List<Alumno> {
    return listaAlumnos.map { alumno ->

        val notaFinal = (alumno.parcial1 * 0.3) + (alumno.parcial2 * 0.3) + (alumno.practicas * 0.4)
        val notaFinalRedondeada = Math.round(notaFinal * 100.0) / 100.0
        alumno.copy(notaFinal = notaFinalRedondeada)
    }
}


// Función para clasificar a los alumnos en aprobados y suspensos

fun clasificarAlumnos(listaAlumnos: List<Alumno>): Pair<List<Alumno>, List<Alumno>> {

    val aprobados = mutableListOf<Alumno>()
    val suspensos = mutableListOf<Alumno>()

    for (alumno in listaAlumnos) {

        // Verifica si el alumno cumple con los criterios para aprobar
        if (alumno.asistencia >= 75.0 &&
            alumno.parcial1 >= 4 &&
            alumno.parcial2 >= 4 &&
            alumno.practicas >= 4 &&
            (alumno.notaFinal ?: 0.0) >= 5) {
            aprobados.add(alumno)
        } else {
            suspensos.add(alumno)
        }
    }
    return Pair(aprobados, suspensos)
}

// Función principal
fun main() {

    val rutaArchivo = "C:\\Users\\Josue\\Documents\\Proyectos\\ADA2\\src\\main\\resources\\calificaciones.csv"
    val listaAlumnos = leerCalificaciones(rutaArchivo)
    val listaConNotasFinales = calcularNotasFinales(listaAlumnos)
    val (aprobados, suspensos) = clasificarAlumnos(listaConNotasFinales)


    println("Aprobados:")
    aprobados.forEach { println(it) }

    println("\nSuspensos:")
    suspensos.forEach { println(it) }
}
