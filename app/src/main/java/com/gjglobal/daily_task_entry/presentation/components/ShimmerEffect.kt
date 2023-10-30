
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.gjglobal.daily_task_entry.R


@Composable
fun ShimmerItemNew(
  isLoading: Boolean, actualContent: @Composable () -> Unit, modifier: Modifier = Modifier
) {
  // If actual content is in loading state , display it shimmer Items
  if (isLoading) {
    Row(modifier = modifier) {
      Column(
        modifier = Modifier
          .weight(1f)
          .align(CenterVertically)
      ) {
        Card(modifier = Modifier
          .fillMaxSize()
          .height(130.dp)
          .padding(
            vertical = dimensionResource(id = R.dimen.dimen_10),
            horizontal = dimensionResource(id = R.dimen.dimen_10)
          ),
          shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_10)),
          elevation = 10.dp){
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .shimmerEffect()

          )
        }

        //Spacer(modifier = Modifier.height(16.dp))
      }
    }
  } else {
    // If actual content is loaded, display it
    actualContent()
  }
}
@Composable
fun ShimmerItem(
  isLoading: Boolean, actualContent: @Composable () -> Unit, modifier: Modifier = Modifier
) {
  // If actual content is in loading state , display it shimmer Items
  if (isLoading) {
    Row(modifier = modifier) {
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .shimmerEffect()
      )
      Spacer(modifier = Modifier.width(16.dp))
      Column(
        modifier = Modifier
          .weight(1f)
          .align(CenterVertically)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(20.dp)
            .shimmerEffect()
        )
      }
    }
  } else {
    // If actual content is loaded, display it
    actualContent()
  }
}
private fun Modifier.shimmerEffect(): Modifier = composed {
  var size by remember {
    mutableStateOf(IntSize.Zero)
  }
  val transition = rememberInfiniteTransition(label = "")
  val startOffsetX by transition.animateFloat(
    initialValue = -2 * size.width.toFloat(),
    targetValue = 2 * size.width.toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(2000)
    ), label = ""
  )
  background(
    brush = Brush.linearGradient(
      colors = listOf(
        Color(0xFFF6F8F8),
        Color(0xFFE7EAEB),
        Color(0xFFF6F8F8),
      ),
      start = Offset(startOffsetX, 0f),
      end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
    )
  ).onGloballyPositioned {
    size = it.size
  }
}

@Composable
fun ShimmerEffectListView(isLoading: Boolean) {
  LazyColumn(
    modifier = Modifier.fillMaxSize()
  ) {
    items(20) {
      ShimmerItemNew(
        isLoading = isLoading,
        actualContent = {  },
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 2.dp)
      )
    }
  }
}