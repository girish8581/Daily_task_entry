package com.gjglobal.daily_task_entry.presentation.utils

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object HomeScreen : Screen("home_screen")
    object NotificationScreen : Screen("notification_screen")
    object MoreScreen : Screen("more_screen")
    object TaskListScreen : Screen("task_list_screen")
    object TaskListCompleteViewScreen : Screen("task_list_complete_view_screen")
    object TaskListToDoViewScreen : Screen("task_list_todo_view_screen")
    object LeaveScreen : Screen("leave_screen")
    object TaskScreen : Screen("task_screen")
    object TaskAssignScreen : Screen("task_assign_screen")

    object ReportScreen : Screen("report_screen")

}

