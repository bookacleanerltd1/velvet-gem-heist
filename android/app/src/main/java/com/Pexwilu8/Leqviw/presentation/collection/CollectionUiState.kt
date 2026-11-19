package com.Pexwilu8.Leqviw.presentation.collection

import com.Pexwilu8.Leqviw.domain.model.CollectionEntry

data class CollectionUiState(
    val entries: List<CollectionEntry> = emptyList(),
    val selected: Int = -1
) {
    val discovered: Int get() = entries.count { it.discovered }
    val total: Int get() = entries.size
    val empty: Boolean get() = discovered == 0
    val detail: CollectionEntry? get() = entries.getOrNull(selected)?.takeIf { it.discovered }
}
