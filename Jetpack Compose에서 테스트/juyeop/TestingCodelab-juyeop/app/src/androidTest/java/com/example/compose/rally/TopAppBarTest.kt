package com.example.compose.rally

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import com.example.compose.rally.ui.theme.RallyTheme
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    // Activity에 접근해야 한다면 createAndroidComposeRule 사용
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun rallyTopAppBarTest() {
        val allScreens = RallyScreen.values().toList()

        // 한번만 setContent를 호출할 수 있다
        composeTestRule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    allScreens = allScreens,
                    onTabSelected = {},
                    currentScreen = RallyScreen.Accounts
                )
            }
        }

        Thread.sleep(5000L)
    }

    @Test
    fun rallyTopAppBarTest_currentTabSelected() {
        val allScreens = RallyScreen.values().toList()

        composeTestRule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    allScreens = allScreens,
                    onTabSelected = {},
                    currentScreen = RallyScreen.Accounts
                )
            }
        }

        // contentDescription이 key 값이 된다
        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
    }

    @Test
    fun rallyTopAppBarTest_currentLabelExists() {
        val allScreens = RallyScreen.values().toList()

        composeTestRule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    allScreens = allScreens,
                    onTabSelected = {},
                    currentScreen = RallyScreen.Accounts
                )
            }
        }

        // UI 트리 구조를 확인할 수 있다
        // 아직 UI 테스트 시에는 Layout Inspector 기능을 제공하지 않는다
        // 컴포저블에는 ID 값이 없다, Modifier의 testTag 또는 hasTestTag를 통해서 이를 대신할 수 있다
        composeTestRule.onRoot().printToLog("currentLabelExists")

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertExists()

        // matcher 함수들을 사용해서 해당하는 노드를 찾는다
        // hasText를 사용할 때는 hasParent와 함께 조합해서 더욱 정확한 값을 쿼리할 수 있도록 한다
        composeTestRule
            .onNode(
                matcher = hasText(RallyScreen.Accounts.name.uppercase()) and
                        hasParent(hasContentDescription(RallyScreen.Accounts.name)),
                useUnmergedTree = true
            )
            .assertExists()
    }

    @Test
    fun rallyTopAppBarTest_changedTabIsSelected() {
        val allScreens = RallyScreen.values().toList()
        var currentScreen by mutableStateOf(RallyScreen.Accounts)

        composeTestRule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    allScreens = allScreens,
                    onTabSelected = { currentScreen = it },
                    currentScreen = currentScreen
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Bills.name)
            .performClick()
            .assertIsSelected()
    }

    @Test
    fun rallyTopAppBarTest_changedTabIsSelected2() {
        composeTestRule.setContent {
            RallyTheme {
                RallyApp()
            }
        }

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Bills.name)
            .performClick()
            .assertIsSelected()
    }
}
