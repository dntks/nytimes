package com.dk.newyorktimes.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dk.newyorktimes.R
import com.dk.newyorktimes.databinding.FragmentArticleSearchBinding
import com.dk.newyorktimes.network.NetworkResult
import com.dk.newyorktimes.ui.adapter.ArticleAdapter
import com.dk.newyorktimes.ui.fragments.ArticleSearchFragmentDirections.Companion.actionArticleSearchFragmentToArticleDetailsFragment
import com.dk.newyorktimes.ui.model.Article
import com.dk.newyorktimes.util.hideKeyboard
import com.dk.newyorktimes.viewmodel.ArticleViewModel

class ArticleSearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentArticleSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleViewModel = ViewModelProvider(requireActivity()).get(ArticleViewModel::class.java)
        articleViewModel.searchArticles()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleSearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        val articleAdapter = ArticleAdapter(::navigateToDetails)
        setupRecyclerView(articleAdapter)
        observeApiData(articleAdapter)
        return binding.root
    }

    private fun setupRecyclerView(articleAdapter: ArticleAdapter) {
        binding.articlesRecyclerView.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.articlesRecyclerView.showShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.characters_menu, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = (searchItem.actionView as SearchView)

        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    private fun navigateToDetails(article: Article) {
        val action = actionArticleSearchFragmentToArticleDetailsFragment(article)
        view?.findNavController()?.navigate(action)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        articleViewModel.searchArticles(query.orEmpty())
        hideKeyboard()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = true

    private fun observeApiData(articleAdapter: ArticleAdapter) {
        articleViewModel.searchedArticles.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> showResult(response, articleAdapter)
                is NetworkResult.Error -> showNetworkError(response)
                is NetworkResult.Loading -> showLoading()
            }
        })
    }

    private fun showLoading() {
        hideUIError()
        binding.articlesRecyclerView.showShimmer()
    }

    private fun showNetworkError(response: NetworkResult<List<Article>>) {
        showUIError(response.message.toString())
        Toast.makeText(
            requireContext(),
            response.message.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showResult(response: NetworkResult<List<Article>>, articleAdapter: ArticleAdapter) {
        hideUIError()
        binding.articlesRecyclerView.hideShimmer()
        val articles = response.data
        if (articles == null || articles.isEmpty()) {
            showUIError(getString(R.string.no_articles_found))
        } else {
            articleAdapter.setArticles(articles)
        }
    }

    private fun hideUIError() {
        binding.articlesRecyclerView.isVisible = true
        binding.errorImageView.isVisible = false
        binding.errorText.isVisible = false
    }

    private fun showUIError(errorText: String) {
        binding.articlesRecyclerView.isVisible = false
        binding.errorImageView.isVisible = true
        binding.errorText.isVisible = true
        binding.errorText.text = errorText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}