package com.glory.devfestuyoandroid_apollo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.glory.devfestuyoandroid_apollo.databinding.ActivityMainBinding
import com.pdl.graghqlexample.MyApoloClient
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.getPostBtn.setOnClickListener{
            progress.visibility = View.VISIBLE
            getAllPost() }
        binding.submitBtn.setOnClickListener{
            progress.visibility = View.VISIBLE
            createPost() }
    }

    //Implementing get request
    private fun getAllPost() {

        MyApoloClient.getMyApoloClient()?.query(AllPostQuery.builder().build())
            ?.enqueue(object : ApolloCall.Callback<AllPostQuery.Data>() {
                override fun onResponse(response: Response<AllPostQuery.Data>) {
                    this@MainActivity.runOnUiThread{
                        if(response.data()?.allPosts?.isEmpty()!!){
                            Toast.makeText(
                                this@MainActivity,
                                "There are no post. Please add some post", Toast.LENGTH_SHORT
                            ).show()
                            progress.visibility = View.GONE

                        }else{
                            progress.visibility = View.GONE
                            binding.postTv.setText(response.data()?.toString())
                        }
                    }

                }

                override fun onFailure(e: ApolloException) {

                }
            })
    }

    //Implementing post request
    private fun createPost() {
        var titleView = binding.title.text.toString()
        var descriptionView = binding.description.text.toString()
        var authorView = binding.author.text.toString()

        if (titleView.isNullOrEmpty() || descriptionView.isNullOrEmpty() || authorView.isNullOrEmpty()) {
            Toast.makeText(
                this@MainActivity, "Fields are required!", Toast.LENGTH_SHORT
            ).show()
            return
        }
        MyApoloClient.getMyApoloClient()?.mutate(
            CreatePostMutation.builder()
                .title(titleView)
                .description(descriptionView)
                .author(authorView).build()
        )?.enqueue(object : ApolloCall.Callback<CreatePostMutation.Data?>() {
            override fun onResponse(response: Response<CreatePostMutation.Data?>) {
                this@MainActivity.runOnUiThread{
                    progress.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Post added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }

            override fun onFailure(e: ApolloException) {

            }
        })
    }

    companion object {
        private val TAG = "MainActivity"
    }
}
