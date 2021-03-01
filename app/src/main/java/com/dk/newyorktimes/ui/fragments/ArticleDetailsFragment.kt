package com.dk.newyorktimes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.dk.newyorktimes.R

class ArticleDetailsFragment : Fragment() {
    private val args by navArgs<ArticleDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val article = args.article
        return inflater.inflate(R.layout.fragment_article_details, container, false)
    }
}