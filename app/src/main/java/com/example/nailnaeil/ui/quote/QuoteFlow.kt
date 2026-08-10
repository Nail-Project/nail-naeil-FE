package com.example.nailnaeil.ui.quote

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.example.nailnaeil.ui.quote.screens.AnalyzingLoadingScreen
import com.example.nailnaeil.ui.quote.screens.ConfirmScreen
import com.example.nailnaeil.ui.quote.screens.PhotoUploadScreen
import com.example.nailnaeil.ui.quote.screens.RangeSelectScreen
import com.example.nailnaeil.ui.quote.screens.RequestSuccessScreen
import com.example.nailnaeil.ui.quote.screens.ScheduleScreen
import com.example.nailnaeil.ui.quote.screens.TreatmentInfoScreen
import com.example.nailnaeil.ui.quote.sheets.RemovalEditSheet
import com.example.nailnaeil.ui.quote.sheets.RequestNoteDialogContent
import com.example.nailnaeil.ui.quote.sheets.ScheduleConfirmDialogContent
import com.example.nailnaeil.ui.quote.sheets.ScheduleEditSheet
import com.example.nailnaeil.ui.quote.sheets.TreatmentPartEditSheet

private enum class EditSheet { NONE, SCHEDULE, REMOVAL, PART }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteFlow(onFinish: () -> Unit, modifier: Modifier = Modifier) {
    val state = remember { QuoteUiState() }
    var step by remember { mutableStateOf(QuoteStep.PHOTO_UPLOAD) }
    var showScheduleConfirmDialog by remember { mutableStateOf(false) }
    var showRequestNoteDialog by remember { mutableStateOf(false) }
    var editSheet by remember { mutableStateOf(EditSheet.NONE) }
    var showRangeEditScreen by remember { mutableStateOf(false) }

    BackHandler(enabled = step != QuoteStep.PHOTO_UPLOAD && step != QuoteStep.SUCCESS) {
        if (showRangeEditScreen) {
            showRangeEditScreen = false
        } else {
            step = when (step) {
                QuoteStep.ANALYZING -> QuoteStep.PHOTO_UPLOAD
                QuoteStep.TREATMENT_INFO -> QuoteStep.PHOTO_UPLOAD
                QuoteStep.SCHEDULE -> QuoteStep.TREATMENT_INFO
                QuoteStep.RANGE_SELECT -> QuoteStep.SCHEDULE
                QuoteStep.CONFIRM -> QuoteStep.RANGE_SELECT
                else -> step
            }
        }
    }

    Box(modifier.fillMaxSize()) {
        when (step) {
            QuoteStep.PHOTO_UPLOAD -> PhotoUploadScreen(
                state = state,
                onClose = onFinish,
                onNext = { step = QuoteStep.ANALYZING }
            )

            QuoteStep.ANALYZING -> AnalyzingLoadingScreen(
                state = state,
                onAnalyzed = { step = QuoteStep.TREATMENT_INFO }
            )

            QuoteStep.TREATMENT_INFO -> TreatmentInfoScreen(
                state = state,
                onBack = { step = QuoteStep.PHOTO_UPLOAD },
                onAddMorePhotos = { step = QuoteStep.PHOTO_UPLOAD },
                onNext = { step = QuoteStep.SCHEDULE }
            )

            QuoteStep.SCHEDULE -> ScheduleScreen(
                state = state,
                onBack = { step = QuoteStep.TREATMENT_INFO },
                onSubmit = { showScheduleConfirmDialog = true }
            )

            QuoteStep.RANGE_SELECT -> RangeSelectScreen(
                state = state,
                onBack = { step = QuoteStep.SCHEDULE },
                onSubmit = { step = QuoteStep.CONFIRM }
            )

            QuoteStep.CONFIRM -> if (showRangeEditScreen) {
                RangeSelectScreen(
                    state = state,
                    onBack = { showRangeEditScreen = false },
                    onSubmit = { showRangeEditScreen = false }
                )
            } else {
                ConfirmScreen(
                    state = state,
                    onBack = { step = QuoteStep.RANGE_SELECT },
                    onShowMoreRequest = { showRequestNoteDialog = true },
                    onChangeSchedule = { editSheet = EditSheet.SCHEDULE },
                    onChangeRemoval = { editSheet = EditSheet.REMOVAL },
                    onChangePart = { editSheet = EditSheet.PART },
                    onChangeRange = { showRangeEditScreen = true },
                    onSubmit = { step = QuoteStep.SUCCESS }
                )
            }

            QuoteStep.SUCCESS -> RequestSuccessScreen(
                neighborhood = state.neighborhood.value,
                onGoToQuoteBox = onFinish
            )
        }
    }

    if (showScheduleConfirmDialog) {
        Dialog(onDismissRequest = { showScheduleConfirmDialog = false }) {
            ScheduleConfirmDialogContent(
                state = state,
                onBackToEdit = { showScheduleConfirmDialog = false },
                onConfirm = {
                    showScheduleConfirmDialog = false
                    step = QuoteStep.RANGE_SELECT
                }
            )
        }
    }

    if (showRequestNoteDialog) {
        Dialog(onDismissRequest = { showRequestNoteDialog = false }) {
            RequestNoteDialogContent(state = state, onDismiss = { showRequestNoteDialog = false })
        }
    }

    if (editSheet != EditSheet.NONE) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(onDismissRequest = { editSheet = EditSheet.NONE }, sheetState = sheetState) {
            when (editSheet) {
                EditSheet.SCHEDULE -> ScheduleEditSheet(state = state, onConfirm = { editSheet = EditSheet.NONE })
                EditSheet.REMOVAL -> RemovalEditSheet(state = state, onConfirm = { editSheet = EditSheet.NONE })
                EditSheet.PART -> TreatmentPartEditSheet(state = state, onConfirm = { editSheet = EditSheet.NONE })
                EditSheet.NONE -> Unit
            }
        }
    }
}
