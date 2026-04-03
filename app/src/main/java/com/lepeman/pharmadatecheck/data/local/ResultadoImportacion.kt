package com.lepeman.pharmadatecheck.data.local

/**
 * Resultado de una operación de importación masiva desde archivo CSV.
 *
 * Usado por [OfflineProductoRepository] y [OfflineLaboratorioRepository]
 * para comunicar al [ConfigViewModel] el resultado de la importación,
 * diferenciando entre una operación exitosa con sus métricas y un error
 * que impidió completarla.
 */
sealed class ResultadoImportacion {

    /**
     * La importación se completó correctamente.
     *
     * @property importados Cantidad de registros efectivamente insertados
     * en la base de datos.
     * @property omitidos Cantidad de registros descartados por duplicidad
     * (ya existían en la tabla) o por formato inválido.
     */
    data class Exito(val importados: Int, val omitidos: Int) : ResultadoImportacion()

    /**
     * La importación falló antes de poder procesar ningún registro.
     *
     * @property mensaje Descripción del error ocurrido, mostrada al usuario
     * en la pantalla de configuración.
     */
    data class Error(val mensaje: String) : ResultadoImportacion()
}