package com.example.myapplication.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.MockData
import com.example.myapplication.MockData.getTimeAgo
import com.example.myapplication.R
import com.example.myapplication.model.TopNewsArticle
import com.example.myapplication.model.getAllArticleCategory
import com.example.myapplication.network.NewsManager
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun Categories(onFetchCategory:(String)->Unit={},newsManager: NewsManager) {
    val tabsItems = getAllArticleCategory()
    Column {
        LazyRow() {
            items(tabsItems.size) {
                val category = tabsItems[it]
                CategoryTab(
                    category = category.categoryName, onFetchCategory = onFetchCategory,
                    isSelected =
                    newsManager.selectedCategory.value == category
                )
            }
        }
        ArticleContent(articles = newsManager.getArticleByCategory.value.articles ?: listOf())
    }
}
@Composable
fun CategoryTab(
    category: String,
    isSelected: Boolean = false,
    onFetchCategory: (String) -> Unit
) {
    val background =
        if (isSelected) colorResource(id = R.color.purple_200)
        else colorResource(id = R.color.purple_700)

    Surface(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
            .clickable {
                onFetchCategory(category)
            },
        shape = MaterialTheme.shapes.small,
        color = background
    )
    {
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.padding(8.dp)

        )
    }
}

@Composable
fun ArticleContent(articles: List<TopNewsArticle>, modifier:Modifier = Modifier) {
    LazyColumn{
        items(articles){article->
            Card(modifier.padding(8.dp),border = BorderStroke(2.dp,color = colorResource(id = R.color.purple_500))) {
                Row(
                    modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                    CoilImage(imageModel = article.urlToImage,modifier = Modifier.size(100.dp),placeHolder = painterResource(
                        id = R.drawable.breaking_news),error = painterResource(
                        id = R.drawable.breaking_news) )
                    Column(modifier.padding(8.dp) ) {
                        Text(text = article.title ?: "Not Available", fontWeight = FontWeight.Bold,
                            maxLines = 3,overflow = TextOverflow.Ellipsis)
                        Row(modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = article.author?:"Not Available")
                            Text(text = MockData.stringToDate(article.publishedAt?:"2021-11-10T14:25:20Z").getTimeAgo())
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ArticleContentPreview() {
    ArticleContent(
        articles =
        listOf(TopNewsArticle(
            author = "Namita Singh",
            title = "Cleo Smith news — live: Kidnap suspect 'in hospital again' as 'hard police grind' credited for breakthrough - The Independent",
            description = "The suspected kidnapper of four-year-old Cleo Smith has been treated in hospital for a second time amid reports he was “attacked” while in custody.",
            publishedAt = "2021-11-04T04:42:40Z"
        )
        ))
}