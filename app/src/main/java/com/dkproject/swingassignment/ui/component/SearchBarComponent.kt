package com.dkproject.swingassignment.ui.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.swingassignment.R
import com.dkproject.swingassignment.ui.theme.SwingAssignmentTheme
import com.dkproject.swingassignment.util.Keyboard
import com.dkproject.swingassignment.util.keyboardState

/** 검색 바 */
@Composable
fun SearchBarComponent(
    modifier: Modifier = Modifier,
    searchText:String,
    searchList: List<String>,
    searchTextChange:(String)->Unit,
    onSearch: (String) -> Unit,
    removeHistory:(String)->Unit,
) {
    //포커스 매니저 ( 가장 최근 포커스 )
    val focusManager = LocalFocusManager.current
    // 키보드 컨트롤러 - 검색 후 키보드 내리기 위함
    val keyboardController = LocalSoftwareKeyboardController.current
    //검색어

    //키보드 상태
    val isKeyboardState by keyboardState()
    //키보드가 열려있는지 닫혀있는지 ?
    var isKeyboardOpend by rememberSaveable {
        mutableStateOf(false)
    }
    //키보드 상태가 바뀔 때마다 -> 열려있다면 최근 검색어 보이게
    LaunchedEffect(key1 = isKeyboardState) {
        isKeyboardOpend = if(isKeyboardState.name == Keyboard.OPENED.name)
            true
        else{ //닫혀있다면 최근 검색어 닫기
            focusManager.clearFocus()
            false
        }
    }


    Column(
        modifier = modifier.background(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
    ) {
        //검색어 필드
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth(),
            text = searchText,
            placeholder = "이미지를 검색해보세요",
            leadingIcon = Icons.Outlined.Search,
            ontextChange = searchTextChange,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
            onAction = KeyboardActions {
                // search 아이콘 누를 시
                keyboardController?.hide()
                onSearch(searchText)
            }
        )
        //키보드가 열려있고, 최근 검색어가 있다면
        AnimatedVisibility(visible = searchList.isNotEmpty() && isKeyboardOpend) {
            LazyColumn {
                items(if(searchList.size>=3)3 else searchList.size){index->
                    val history:String = searchList[index]
                    SearchHistoryItem(modifier=Modifier.clickable {
                        keyboardController?.hide()
                        onSearch(history)
                    },text = history,
                        removeHistory = removeHistory)
                }
            }
        }
    }
}

