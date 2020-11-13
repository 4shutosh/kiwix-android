/*
 * Kiwix Android
 * Copyright (c) 2020 Kiwix <android.kiwix.org>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.kiwix.kiwixmobile.core.page.notes.viewmodel

import org.kiwix.kiwixmobile.core.base.SideEffect
import org.kiwix.kiwixmobile.core.dao.NewNoteDao
import org.kiwix.kiwixmobile.core.page.notes.adapter.NoteItem
import org.kiwix.kiwixmobile.core.page.notes.viewmodel.effects.UpdateAllNotesPreference
import org.kiwix.kiwixmobile.core.page.viewmodel.Action
import org.kiwix.kiwixmobile.core.page.viewmodel.PageViewModel
import org.kiwix.kiwixmobile.core.reader.ZimReaderContainer
import org.kiwix.kiwixmobile.core.utils.SharedPreferenceUtil
import javax.inject.Inject

class NotesViewModel @Inject constructor(
  notesDao: NewNoteDao,
  zimReaderContainer: ZimReaderContainer,
  sharedPrefs: SharedPreferenceUtil
) : PageViewModel<NoteItem, NotesState>(notesDao, sharedPrefs, zimReaderContainer) {
  override fun initialState(): NotesState =
    NotesState(emptyList(), sharedPreferenceUtil.showNotesAllBooks, zimReaderContainer.id)

  override fun updatePagesBasedOnFilter(state: NotesState, action: Action.Filter): NotesState =
    state.copy(searchTerm = action.searchTerm)

  override fun updatePages(state: NotesState, action: Action.UpdatePages): NotesState =
    state.copy(pageItems = action.pages.filterIsInstance<NoteItem>())

  override fun offerUpdateToShowAllToggle(
    action: Action.UserClickedShowAllToggle,
    state: NotesState
  ): NotesState {
    effects.offer(UpdateAllNotesPreference(sharedPreferenceUtil, action.isChecked))
    return state.copy(showAll = action.isChecked)
  }

  override fun copyWithNewItems(state: NotesState, newItems: List<NoteItem>): NotesState =
    state.copy(pageItems = newItems)

  override fun deselectAllPages(state: NotesState): NotesState =
    state.copy(pageItems = state.pageItems.map { it.copy(isSelected = false) })

  override fun createDeletePageDialogEffect(state: NotesState): SideEffect<*> {
    TODO("Not yet implemented")
    // implement delete note or something like that
  }
}
