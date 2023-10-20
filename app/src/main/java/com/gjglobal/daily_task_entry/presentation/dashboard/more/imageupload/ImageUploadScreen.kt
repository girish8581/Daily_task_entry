import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import com.gjglobal.daily_task_entry.presentation.components.ToastMessage
import com.gjglobal.daily_task_entry.presentation.components.ToolBar
import com.gjglobal.daily_task_entry.presentation.dashboard.DashboardViewModel
import com.gjglobal.daily_task_entry.presentation.theme.ColorPrimary
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_400_12
import com.gjglobal.daily_task_entry.presentation.theme.TextStyle_800_18
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageUploadScreen(
    navController: NavController,
    viewModel: DashboardViewModel,
    activity : Activity
) {
    val  contentResolver = viewModel.contentResolver
    var errorToast by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUri by remember { mutableStateOf<Uri?>(null) }

    Column {
        ToolBar(nameOfScreen = "Image Upload",iconOfScreen = 0, onClick = {
            navController.popBackStack() }, onIconClick = {})

        Spacer(modifier = Modifier.height(30.dp))

        AppContent(navController,contentResolver,viewModel,activity)


    }


    if(viewModel.state.value.isImageUploaded){
        ToastMessage(context = activity.applicationContext, message = "Image uploaded successfully")

    }
}

// Load a bitmap from a URI
private fun loadBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    return try {
        val inputStream = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppContent(navController: NavController,contentResolver:ContentResolver,viewModel: DashboardViewModel,activity:Activity) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var loading by remember { mutableStateOf(false) }
    var uriLoad by remember { mutableStateOf(false) }
    var state = viewModel.state.value
    val cacheManager :CacheManager = CacheManager(activity.applicationContext)

    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri

        Log.e("imageuri",selectedImageUri.toString())
    }

    val scope = rememberCoroutineScope()

    bitmap = selectedImageUri?.let { loadBitmapFromUri(contentResolver, it) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {


//        Text(
//            text = "Image Upload",
//            style = TextStyle_800_18,
//            modifier = Modifier.padding(16.dp)
//        )

        Spacer(modifier = Modifier.height(30.dp))

        if (bitmap != null) {
            ImageLoad(selectedImageUri.toString())
        } else {
            Text(
                text = "No image selected.\nPlease use below 1mb size PNG file",
                style = TextStyle_400_12,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = ColorPrimary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                getContent.launch("image/*")
                // Launch image picker
                // Ensure you have the appropriate permissions
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Choose Image")
        }

        Button(
            onClick = {

                selectedImageUri?.let {

                                println("local uri"+it)
                    viewModel.uploadProfilePicture(activity = activity,uri = it,cacheManager.getAuthResponse()!!.data[0].id)
                }

                loading = true
                uriLoad = true


            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Upload Image")
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        }
    }

    if(uriLoad){

        selectedImageUri?.let {



            loading = true

            loadBitmapAsync(uri= it, contentResolver = contentResolver) { loadedBitmap ->

                bitmap = loadedBitmap



                val inputStream = contentResolver.openInputStream(it)
                val byteArrayOutputStream = ByteArrayOutputStream()
                inputStream?.use {
                    it.copyTo(byteArrayOutputStream)
                }
                val imageBytes = byteArrayOutputStream.toByteArray()

                // Upload the image using your API (replace with your API endpoint)
                //function to be created here
               // viewModel.uploadImageToApi(imageBytes)

            }
        }

    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun loadBitmapAsync(uri: Uri, contentResolver: ContentResolver, callback: (Bitmap?) -> Unit) {
    val scope = rememberCoroutineScope()
    scope.launch {
        val bitmap = withContext(Dispatchers.IO) {
            try {
                val inputStream = contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    BitmapFactory.decodeStream(inputStream)
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        callback(bitmap)
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageLoad(uri:String) {
    val imageUrl = uri// Replace with your image URL or Uri

    Image(
        painter = rememberImagePainter(data = imageUrl), // Provide the image URL or Uri here
        contentDescription = "Image",
        modifier = Modifier.height(250.dp)
    )
}




