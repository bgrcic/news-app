package com.example.myapplication.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.TopNewsArticle
import com.example.myapplication.network.NewsManager
import com.example.myapplication.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sources(newsManager: NewsManager) {
    val items = listOf(
        "TechCrunch" to "techcrunch",
        "TalkSport" to "talksport",
        "Business Insider" to "business-insider",
        "Reuters" to "reuters",
        "Politico" to "politico",
        "TheVerge" to "the-verge"
    )
    Scaffold(topBar={
        //Todo 3: Pass in TopAppBar, set the title to the source name and add drop down as an actions
        TopAppBar(
            title = {
                Text(text = "${newsManager.sourceName.value} Source")
            },
            actions = {
                //Todo 4: we create a remember variable to control the show and dismisgs of the drop down
                var menuExpanded by remember { mutableStateOf(false) }

                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }
                MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))) {
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            menuExpanded = false
                        },
                    ) {
                        items.forEach {
                            DropdownMenuItem(onClick = {
                                newsManager.sourceName.value = it.second
                                menuExpanded = false
                            }) {
                                Text(it.first)
                            }
                        }
                    }
                }
            }
        )}){

        newsManager.getArticlesBySource()
        val article = newsManager.getArticleBySource.value
        //Todo 13: Add the Content composable and pass in articles by source
        SourceContent(articles = article.articles?: listOf() )

    }
}

//Todo 8: Create a composable to display the articles
@Composable
fun SourceContent(articles:List<TopNewsArticle>) {
    val uriHandler = LocalUriHandler.current
    LazyColumn{
        items(articles) { article ->
            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(
                    tag = "URL",
                    annotation = article.url ?: "newsapi.org"
                )
                withStyle(style = SpanStyle(color = colorResource(id = R.color.purple_500),textDecoration = TextDecoration.Underline)) {
                    append("Read Full Article Here")
                }
                pop()
            }
            Card(backgroundColor = colorResource(id = R.color.purple_700),elevation = 6.dp, modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier
                    .height(200.dp)
                    .padding(end = 8.dp, start = 8.dp),verticalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = article.title ?: "Not Available",
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = article.description ?: "Not Available",
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    /**Todo 11 we use a card to set hyperlink for the url to the full article
                     * We use a ClickbaleText instead of Text so we can set an action for when its clicked
                     * Pass in the annotated string as text and get its result as a url then open with the uri handler
                     */
                    Card(
                        backgroundColor = colorResource(id = R.color.white),
                        elevation = 6.dp,
                    ) {
                        ClickableText(text = annotatedString,
                            modifier = Modifier.padding(8.dp),
                            onClick = {
                                annotatedString.getStringAnnotations(it,it).firstOrNull()
                                    ?.let { result ->
                                        if (result.tag == "URL") {
                                            uriHandler.openUri(result.item)
                                        }
                                    }
                            })
                    }
                }
            }
        }}}

//Todo 12: create preview for source content
@Preview(showBackground = true)
@Composable
fun SourceContentPreview() {
    SourceContent(
        articles = listOf(
            TopNewsArticle(
                author = "Namita Singh",
                title = "Cleo Smith news — live: Kidnap suspect 'in hospital again' as 'hard police grind' credited for breakthrough - The Independent",
                description = "The suspected kidnapper of four-year-old Cleo Smith has been treated in hospital for a second time amid reports he was “attacked” while in custody.",
                publishedAt = "2021-11-04T04:42:40Z"
            )

        )
    )
}