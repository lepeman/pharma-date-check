package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la entidad [Empresa].
 *
 * Provee acceso a la tabla "empresas" de la base de datos local.
 * Las operaciones de escritura son suspendidas para ejecutarse fuera
 * del hilo principal. Las consultas reactivas retornan [Flow] y se
 * actualizan automáticamente ante cambios en la tabla.
 */
@Dao
interface EmpresaDao {

    /**
     * Retorna todas las empresas registradas como flujo reactivo.
     * Se actualiza automáticamente cuando la tabla cambia.
     */
    @Query("SELECT * FROM empresas")
    fun obtenerTodas(): Flow<List<Empresa>>

    /**
     * Retorna la razón social de la empresa con el [id] indicado.
     * Usado para resolver el nombre de empresa en formularios y tarjetas.
     */
    @Query("SELECT razonSocial FROM empresas WHERE id = :id")
    suspend fun obtenerNombreEmpresa(id: Int): String

    /**
     * Retorna el id de la empresa cuya razón social coincide exactamente
     * con [razonSocial], o null si no existe.
     */
    @Query("SELECT id FROM empresas WHERE razonSocial = :razonSocial")
    suspend fun obtenerIdPorNombre(razonSocial: String): Int?

    /**
     * Retorna como flujo reactivo las empresas cuya razón social contiene
     * [query] como subcadena. Usado por el autocompletado del formulario
     * de políticas de canje.
     */
    @Query("SELECT * FROM empresas WHERE razonSocial LIKE '%' || :query || '%'")
    fun buscarItems(query: String): Flow<List<Empresa>>

    /**
     * Actualiza los datos de una empresa existente en la base de datos.
     */
    @Update
    suspend fun actualizar(empresa: Empresa)

    /**
     * Inserta una empresa. Si ya existe un registro con el mismo id,
     * la operación se ignora (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(empresa: Empresa)

    /**
     * Inserta una lista de empresas de forma masiva. Usado durante
     * el prepoblado inicial de la base de datos en [AppDatabase].
     * Los registros duplicados se ignoran (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodasLasEmpresas(list: List<Empresa>)

    /**
     * Elimina una empresa de la base de datos.
     */
    @Delete
    suspend fun eliminar(empresa: Empresa)
}