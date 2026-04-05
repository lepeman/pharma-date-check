package com.lepeman.pharmadatecheck.data.repositories

import android.content.Context
import android.net.Uri
import com.lepeman.pharmadatecheck.data.local.ResultadoImportacion
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio para la gestión del catálogo de laboratorios.
 *
 * Define las operaciones disponibles sobre la entidad [Laboratorio].
 * La implementación concreta es [OfflineLaboratorioRepository], que delega
 * en [LaboratorioDao] para el acceso a la base de datos local Room e
 * implementa la importación masiva desde archivos CSV.
 */
interface LaboratorioRepository {

    /** Retorna todos los laboratorios registrados como flujo reactivo. */
    fun obtenerTodos(): Flow<List<Laboratorio>>

    /** Retorna el nombre del laboratorio con el [id] indicado, o null si no existe. */
    suspend fun obtenerNombrePorId(id: Int): String?

    /**
     * Retorna como flujo reactivo los laboratorios cuyo nombre contiene
     * [query] como subcadena. Usado por el autocompletado del formulario
     * de políticas de canje.
     */
    fun buscarItems(query: String): Flow<List<Laboratorio>>

    /**
     * Retorna el id del laboratorio cuyo nombre coincide exactamente con [nombre].
     * Usado al guardar una política de canje para obtener el identificador
     * a partir del nombre seleccionado en el formulario.
     */
    suspend fun obtenerIdPorNombre(nombre: String): Int

    /** Actualiza los datos de un laboratorio existente. */
    suspend fun actualizar(laboratorio: Laboratorio)

    /** Inserta un nuevo laboratorio. Si ya existe con el mismo id, se ignora. */
    suspend fun insertar(laboratorio: Laboratorio)

    /** Elimina un laboratorio de la base de datos. */
    suspend fun eliminar(laboratorio: Laboratorio)

    /**
     * Retorna la cantidad total de laboratorios registrados en la tabla.
     * Usado para calcular el número de registros efectivamente importados
     * tras una operación de carga CSV.
     */
    suspend fun contarLaboratorios(): Int

    /**
     * Importa laboratorios desde un archivo CSV seleccionado por el usuario.
     * Retorna [ResultadoImportacion.Exito] con los contadores de registros
     * importados y omitidos, o [ResultadoImportacion.Error] si el archivo
     * no contiene registros válidos.
     *
     * Formato esperado del CSV: CODIGO_LABORATORIO;NOMBRE_LABORATORIO
     *
     * @param context Contexto necesario para acceder al archivo mediante
     * el [android.content.ContentResolver].
     * @param uri URI del archivo CSV seleccionado por el usuario.
     */
    suspend fun insertarLaboratoriosDesdeCSV(context: Context, uri: Uri): ResultadoImportacion
}