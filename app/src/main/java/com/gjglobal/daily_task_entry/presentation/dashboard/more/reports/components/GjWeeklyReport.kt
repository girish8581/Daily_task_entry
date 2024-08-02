package com.gjglobal.daily_task_entry.presentation.dashboard.more.reports.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.gjglobal.daily_task_entry.BuildConfig
import com.gjglobal.daily_task_entry.R
import com.gjglobal.daily_task_entry.presentation.components.ToastMessage
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_500_12
import com.gjglobal.daily_task_entry.presentation.utils.fileTime
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects


@Composable
fun GjWeeklyReport(list: List<ReportModel?>) {
    var isExport by remember { mutableStateOf(false) }
    val context = LocalContext.current

    println("weekly inside $list")

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){

        //ExcelButtonCall(list)

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(list) { item ->
                if (item!!.taskId != null) {
                    ReportRow(reportModel = item)
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }

        Box(modifier = Modifier.fillMaxSize()){
            Row (verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(end = 50.dp, bottom = 100.dp).fillMaxSize(),
                horizontalArrangement = Arrangement.End){
                FloatingActionButton(
                    onClick = { createExcelSpreadsheet(context, list)
                        isExport = true}
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = " export"
                    )
                }
            }

        }
    }

    if(isExport){
        ToastMessage(context = LocalContext.current, message = "Exporting to excel..." )
        isExport = false
    }

}


@Composable
fun YearWeekDropdown() {
    var selectedYear by remember { mutableStateOf(LocalDate.now().year) }
    var selectedWeek by remember { mutableStateOf(LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear())) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Selected Year: $selectedYear, Selected Week: $selectedWeek")

        getStartAndEndDate(year = selectedYear, weekNumber = selectedWeek)

        Spacer(modifier = Modifier.height(16.dp))

        // Year Selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Display current year
            Text(
                text = selectedYear.toString(),
                fontWeight = FontWeight.Bold
            )

            // Year Dropdown
            var expandedYear by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { expandedYear = true }
                    .padding(8.dp)
            ) {
                Text("Select Year", color = Color.White)
                DropdownMenu(
                    expanded = expandedYear,
                    onDismissRequest = { expandedYear = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    for (year in 2021..LocalDate.now().year) {
                        DropdownMenuItem(
                            text = { Text(text = year.toString())},

                            onClick = {
                                selectedYear = year
                                selectedWeek = 1 // Reset selected week when year changes
                                expandedYear = false
                            }
                        )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Week Number Dropdown
        var expandedWeek by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                .clickable { expandedWeek = true }
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Week Number: $selectedWeek")

                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }

            DropdownMenu(
                expanded = expandedWeek,
                onDismissRequest = { expandedWeek = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
            ) {
                val weeksInYear = getWeeksInYear(selectedYear)

                for (week in 1..weeksInYear) {
                    DropdownMenuItem(
                        text= {
                            Text(text = "Week $week")
                        },
                        onClick = {
                            selectedWeek = week
                            expandedWeek = false
                        }
                    )
                }
            }
        }
    }


fun getStartAndEndDate(year: Int, weekNumber: Int): Pair<LocalDate, LocalDate> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    val startDate = LocalDate.of(year, 1, 1)
        .with(firstDayOfWeek)
        .plusWeeks(weekNumber.toLong() - 1)

    val endDate = startDate.plusDays(6)
    print("output$startDate $endDate")
    return Pair(startDate, endDate)

}


fun getWeeksInYear(year: Int): Int {
    val firstDayOfYear = LocalDate.of(year, 1, 1)
    val lastDayOfYear = LocalDate.of(year, 12, 31)
    return lastDayOfYear.get(WeekFields.ISO.weekOfWeekBasedYear())
}

@Composable
fun WeekNumberDropdown() {
    var selectedYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedWeek by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Selected Year-Month: ${selectedYearMonth.format(DateTimeFormatter.ofPattern("MMM yyyy"))}")

        Spacer(modifier = Modifier.height(16.dp))

        // Year and Month Selection
//        MonthYearSelection(
//            selectedYearMonth = selectedYearMonth,
//            onDateSelected = { yearMonth ->
//                selectedYearMonth = yearMonth
//                selectedWeek = 1 // Reset selected week when year or month changes
//            }
//        )

        //YearMonthDropdown()

        Spacer(modifier = Modifier.height(16.dp))

        // Week Number Dropdown
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                .clickable { expanded = true }
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Week Number: $selectedWeek")

                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
            ) {
                val weeksInMonth = getWeeksInMonth(selectedYearMonth)

                for (week in 1..weeksInMonth) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Week $week")
                        },
                        onClick = {
                            selectedWeek = week
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun YearMonthDropdown() {
    var selectedYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedWeek by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Selected Year-Month: ${selectedYearMonth.format(DateTimeFormatter.ofPattern("MMM yyyy"))}")

        Spacer(modifier = Modifier.height(16.dp))

        // Year and Month Selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Previous Month
            IconButton(onClick = {
                val previousMonth = selectedYearMonth.minusMonths(1)
                selectedYearMonth = previousMonth
            }) {
                Icon(Icons.Default.RotateLeft, contentDescription = "Previous Month")
            }

            // Display current month and year
            Text(
                text = selectedYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                fontWeight = FontWeight.Bold
            )

            // Next Month
            IconButton(onClick = {
                val nextMonth = selectedYearMonth.plusMonths(1)
                selectedYearMonth = nextMonth
            }) {
                Icon(Icons.Default.RotateRight, contentDescription = "Next Month")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Week Number Dropdown
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                .clickable { expanded = true }
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Week Number: $selectedWeek")

                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
            ) {
                val weeksInMonth = getWeeksInMonth(selectedYearMonth)

                for (week in 1..weeksInMonth) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Week $week")
                        },
                        onClick = {
                            selectedWeek = week
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


//@Composable
//fun MonthYearSelection(selectedYearMonth: YearMonth, onDateSelected: (YearMonth) -> Unit) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        // Previous Month
//        IconButton(onClick = {
//            val previousMonth = selectedYearMonth.minusMonths(1)
//            onDateSelected(previousMonth)
//        }) {
//            Icon(Icons.Default.ArrowDropDown, contentDescription = "Previous Month")
//        }
//
//        // Display current month and year
//        Text(
//            text = selectedYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
//            fontWeight = FontWeight.Bold
//        )
//
//        // Next Month
//        IconButton(onClick = {
//            val nextMonth = selectedYearMonth.plusMonths(1)
//            onDateSelected(nextMonth)
//        }) {
//            Icon(Icons.Default.ArrowDropDown, contentDescription = "Next Month", modifier = Modifier.rotate(180f))
//        }
//    }
//}

fun getWeeksInMonth(yearMonth: YearMonth): Int {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    return lastDayOfMonth.get(WeekFields.ISO.weekOfMonth()) - firstDayOfMonth.get(WeekFields.ISO.weekOfMonth()) + 1
}

@Composable
fun GjMonthlyReport(list: List<ReportModel?>) {
    var isExport by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){

        //ExcelButtonCall(list)

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(list) { item ->
                if (item != null) {
                    ReportRow(reportModel = item)
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }

        Box(modifier = Modifier.fillMaxSize()){
            Row (verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(end = 50.dp, bottom = 100.dp).fillMaxSize(),
                horizontalArrangement = Arrangement.End){
                FloatingActionButton(
                    onClick = { createExcelSpreadsheet(context, list)
                        isExport = true}
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = " export"
                    )
                }
            }

        }
    }

    if(isExport){
        ToastMessage(context = LocalContext.current, message = "Exporting to excel..." )
        isExport = false
    }

}
@Composable
fun ExcelButtonCall(
    name: List<ReportModel>
) {
    val context = LocalContext.current
    // Creates a button with a blue background color and white text, and sets its onClick listener to
    // call the createExcelSpreadsheet function and pass in the current context and the list of names
    // as parameters.

    Box(
        modifier = Modifier.clickable {
            // Handle click event here
            createExcelSpreadsheet(context, name)
        }
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Image(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, bottom = 10.dp),
                painter = painterResource(id = R.drawable.report),
                contentDescription = stringResource(R.string.excel)
            )

            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
                    .padding(end = 30.dp),
                text = "Excel Report",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun ReportRow(reportModel: ReportModel){
    Box(modifier = Modifier
        .padding(10.dp)
        .border(1.dp, Color.Black)
        .fillMaxWidth()){
        Column(modifier = Modifier
            .padding(5.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                Row(horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "TaskId :", style = TextStyle_500_12)
                    reportModel.taskId?.let { Text(text = it, style = TextStyle_500_12) }
                }

                Row(horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "TaskNo :", style = TextStyle_500_12)
                    reportModel.taskNo?.let { Text(text = it, style = TextStyle_500_12) }
                }
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "TaskName :", style = TextStyle_500_12)
                reportModel.taskName?.let { Text(text = it, style = TextStyle_500_12) }
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Name :", style = TextStyle_500_12)
                    reportModel.resourceName?.let { Text(text = it, style = TextStyle_500_12) }
                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Time Line :", style = TextStyle_500_12)
                    Text(text = reportModel.timeLine.toString(), style = TextStyle_500_12)
                }
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Completion Status :", style = TextStyle_500_12)
                    reportModel.completionStatus?.let { Text(text = it, style = TextStyle_500_12) }
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "QA Acceptance :", style = TextStyle_500_12)
                    reportModel.qaAcceptance?.let { Text(text = it, style = TextStyle_500_12) }
                }
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "TL Acceptance", style = TextStyle_500_12)
                    reportModel.tlAcceptance?.let { Text(text = it, style = TextStyle_500_12) }
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Notes", style = TextStyle_500_12)
                    reportModel.notes?.let { Text(text = it, style = TextStyle_500_12) }
                }
            }

        }

    }

}

private fun createExcelSpreadsheet(context: Context, reportList: List<ReportModel?>) {
    // Create a new Excel workbook
    val workbook = XSSFWorkbook()
    // Create a new sheet in the workbook
    val sheet = workbook.createSheet("WeeklyReport")
    // Add some data to the sheet
    val headers = arrayOf("TASK ID", "TASK NO","TASK NAME","RESOURCE NAME",
        "TIME LINE(HRS)", "COMPLETION STATUS","QA ACCEPTANCE","TL ACCEPTANCE","NOTES")

    // Add the headers to the first row
    val headerRow = sheet.createRow(0)
    headerRow.heightInPoints = 40f
    for (i in headers.indices) {
        val cell = headerRow.createCell(i)
        cell.setCellValue(headers[i])
    }

    // Add the data to subsequent rows
    for (i in reportList.indices) {
        val dataRow = sheet.createRow(i + 1)
       // dataRow.heightInPoints = 40f
//        sheet.setColumnWidth(0, 15 * 500)
//        sheet.setColumnWidth(1, 15 * 500)
        val taskId = dataRow.createCell(0)
        val taskNo = dataRow.createCell(1)
        val taskName = dataRow.createCell(2)
        val resourceName = dataRow.createCell(3)
        val timeLine = dataRow.createCell(4)
        val completionStatus = dataRow.createCell(5)
        val qaAcceptance = dataRow.createCell(6)
        val tlAcceptance = dataRow.createCell(7)
        val notes = dataRow.createCell(8)

        //val timeInt:Double  = (timeStringToInt(reportList[i].timeLine)/60).toDouble()

        taskId.setCellValue(reportList[i]?.taskId ?: "NA")
        taskNo.setCellValue(reportList[i]?.taskNo ?: "NA")
        taskName.setCellValue(reportList[i]?.taskName ?: "NA")
        resourceName.setCellValue(reportList[i]?.resourceName ?:"NA")
        timeLine.setCellValue(reportList[i]?.timeLine.toString())
        completionStatus.setCellValue(reportList[i]?.completionStatus ?: "NA")
        qaAcceptance.setCellValue(reportList[i]?.qaAcceptance ?: "NA")
        tlAcceptance.setCellValue(reportList[i]?.tlAcceptance ?:"NA" )
        notes.setCellValue(reportList[i]?.notes ?: "NA")
    }

    // Write the workbook to a file
    val file = File(context.getExternalFilesDir(null), "WeeklyReport"+ fileTime())
    val outputStream = FileOutputStream(file)
    workbook.write(outputStream)
    outputStream.close()

    // Open the Excel with a Excel viewer app
    val intent = Intent(Intent.ACTION_VIEW)
    val photoURI = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + context.getString(R.string.provider),
        file
    )
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setDataAndType(photoURI, context.getString(R.string.excel))
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }

}

fun timeStringToInt(timeString: String): Int {
    val sdf = SimpleDateFormat("HH:mm")
    val date: Date = sdf.parse(timeString)
    val calendar = Calendar.getInstance()
    calendar.time = date

    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)

    // Calculate the total minutes
    return hours * 60 + minutes
}

data class ReportModel(
    val taskId:String?=null, val taskNo:String?=null, val taskName:String?=null,
    val resourceName:String?=null, val timeLine:Double,
    val completionStatus:String?=null, val qaAcceptance: String?=null, val tlAcceptance: String?=null,
    val notes:String?=null)

