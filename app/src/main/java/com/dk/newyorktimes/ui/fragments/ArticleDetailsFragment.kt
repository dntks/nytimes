package com.dk.newyorktimes.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.dk.newyorktimes.R
import com.dk.newyorktimes.databinding.FragmentArticleDetailsBinding
import com.dk.newyorktimes.ui.model.Article

class ArticleDetailsFragment : Fragment() {

    private var _binding: FragmentArticleDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ArticleDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)

        val article = args.article

        binding.headline.text = article.headline
        binding.byLine.text = article.byLine ?: ""
        binding.weburl.text = article.webUrl
        binding.shareButton.setOnClickListener { shareContent(article) }
        binding.abstractTextView.text = article.abstract
        binding.date.text = article.publishDate ?: ""
        binding.articleImage.load(article.imageUrl) {
            crossfade(300)
            error(R.drawable.ic_error_placeholder)
        }
        return binding.root
    }

    private fun shareContent(article: Article) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text, article.webUrl))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_title))
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}