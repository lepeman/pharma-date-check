package com.lepeman.pharmadatecheck.ui.theme

import androidx.compose.ui.graphics.Color

// ── Colores corporativos Cruz Verde ──────────────────────────────────────────
// Extraídos del SVG oficial del logotipo (Logotipo_Cruz_Verde.svg)

/** Verde principal del logotipo de Cruz Verde. */
val CruzVerdeGreen  = Color(0xFF00953B)

/** Amarillo del logotipo de Cruz Verde. */
val CruzVerdeYellow = Color(0xFFFFD600)

/** Blanco corporativo del logotipo de Cruz Verde. */
val CruzVerdeWhite  = Color(0xFFFFFFFF)

// ── Clasificación de productos ───────────────────────────────────────────────
// Usados por ClasificadorProducto, TarjetaResultado y ChipResumen.

/** Verde Cruz Verde — producto dentro de su período de validez. */
val ColorVigente   = Color(0xFF00953B)

/**
 * Amarillo oscurecido respecto al corporativo (#FFD600) para garantizar
 * legibilidad sobre fondo blanco manteniendo el tono dorado de Cruz Verde.
 */
val ColorCanjeable = Color(0xFFE6A800)

/** Rojo semántico — producto vencido, debe retirarse del inventario. */
val ColorVencido   = Color(0xFFD32F2F)

// ── Superficies y fondos ─────────────────────────────────────────────────────

/** Fondo principal de la aplicación. Blanco puro, coherente con la web de Cruz Verde. */
val ColorFondo = Color(0xFFFFFFFF)

/** Color de fondo de tarjetas y contenedores elevados. */
val ColorCard  = Color(0xFFF5F5F5)

/** Color de bordes, divisores y líneas separadoras. */
val ColorBorde = Color(0xFFE0E0E0)

// ── Tipografía ───────────────────────────────────────────────────────────────

/** Color principal de texto — negro casi puro para máxima legibilidad sobre fondo blanco. */
val ColorTexto = Color(0xFF1A1A1A)

/** Color de texto secundario — gris medio para etiquetas, placeholders y metadata. */
val ColorDim   = Color(0xFF757575)