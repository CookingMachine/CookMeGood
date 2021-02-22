package com.cookMeGood.makeItTasteIt.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookMeGood.makeItTasteIt.R
import com.cookMeGood.makeItTasteIt.adapter.listener.OnOpenRecipeListener
import com.cookMeGood.makeItTasteIt.adapter.listener.OnSearchIngredientClickListener
import com.cookMeGood.makeItTasteIt.adapter.recyclerview.SearchContentAdapter
import com.cookMeGood.makeItTasteIt.adapter.recyclerview.SearchFilterAdapter
import com.cookMeGood.makeItTasteIt.adapter.recyclerview.SearchIngredientsAdapter
import com.cookMeGood.makeItTasteIt.api.ApiService
import com.cookMeGood.makeItTasteIt.api.dto.Category
import com.cookMeGood.makeItTasteIt.api.dto.Ingredient
import com.cookMeGood.makeItTasteIt.api.dto.Recipe
import com.cookMeGood.makeItTasteIt.utils.IntentContainer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.RangeSlider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_recipe_bottom_sheet.*
import kotlinx.android.synthetic.main.content_search_bottom_sheet.*
import kotlinx.android.synthetic.main.content_search_ingredients.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class SearchActivity : SuperActivity() {

    private var caloriesValueFrom = 0
    private var caloriesValueTo = 2000
    private var timeValueFrom = 5
    private var timeValueTo = 120
    private var ingredientsCounter = 0
    private var searchQueryText: String = ""

    private var filterCategoriesList = listOf("Супы", "Горячее", "Сладкое", "Напитки", "Закуски")
    private var filterKitchenList = listOf("Грузинская", "Китайская", "Русская", "Немецкая", "Абхазская")
    private var filterCategoryAdapter: SearchFilterAdapter? = null
    private var filterKitchenAdapter: SearchFilterAdapter? = null
    private var searchContentAdapter: SearchContentAdapter? = null
    private var searchIngredientsAdapter: SearchIngredientsAdapter? = null
    private var clickedIngredientsList: List<Ingredient>? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var searchContentList = listOf(
            Recipe(null, "Рецепт", "Описание рецепта Описание рецепта Описание рецепта Описание рецепта", null, null, null, "time", null, "Кухня"),
            Recipe(null, "Рецепт", "Описание рецепта Описание рецепта Описание рецепта Описание рецепта", null, null, null, "time", null, "Кухня"),
            Recipe(null, "Рецепт", "Описание рецепта Описание рецепта Описание рецепта Описание рецепта", null, null, null, "time", null, "Кухня"),
            Recipe(null, "Рецепт", "Описание рецепта Описание рецепта Описание рецепта Описание рецепта", null, null, null, "time", null, "Кухня"),
            Recipe(null, "Рецепт", "Описание рецепта Описание рецепта Описание рецепта Описание рецепта", null, null, null, "time", null, "Кухня"),
            Recipe(null, "Рецепт", "Описание рецепта Описание рецепта Описание рецепта Описание рецепта", null, null, null, "time", null, "Кухня"),
            Recipe(null, "Рецепт", "Описание рецепта Описание рецепта Описание рецепта Описание рецепта", null, null, null, "time", null, "Кухня"),
            Recipe(null, "Рецепт", "Описание рецепта Описание рецепта Описание рецепта Описание рецепта", null, null, null, "time", null, "Кухня")
    )
    private var searchIngredientsList = listOf(
            Ingredient("Абрикос", "10"),
            Ingredient("Арбуз", "10"),
            Ingredient("Банан", "10"),
            Ingredient("Виноград", "10"),
            Ingredient("Горох", "10"),
            Ingredient("Огурец", "10"),
            Ingredient("Морковь", "10"),
            Ingredient("Катрофель", "10")
    )

    private val openRecipeListener = object : OnOpenRecipeListener {
        override fun openRecipe(recipe: Recipe) {
            val intent = Intent(applicationContext, RecipeActivity::class.java)
            intent.putExtra(IntentContainer.INTENT_RECIPE, recipe)
            startActivity(intent)
        }
    }

    private val onIngredientClickListener = object : OnSearchIngredientClickListener {
        override fun onClick(ingredients: List<Ingredient>) {
            clickedIngredientsList = ingredients
            searchIngredientsCounter.text = clickedIngredientsList!!.size.toString()
        }
    }

    override fun setAttr() = setLayout(R.layout.activity_search)

    @SuppressLint("SetTextI18n")
    override fun initInterface() {

        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val screenHeight = point.y

        setSupportActionBar(searchActivityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        searchBackContent.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        if (getSearchListFromServer("f").isEmpty()) {
            searchActivityContentList.visibility = View.GONE
            searchNotFound.visibility = View.VISIBLE
        } else {
            searchActivityContentList.visibility = View.VISIBLE
            searchNotFound.visibility = View.GONE
        }

        bottomSheetBehavior = BottomSheetBehavior.from(searchBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.isDraggable = false
        bottomSheetBehavior.peekHeight = screenHeight - searchBackContent.measuredHeight - 360
        onChangeSheetHeight(screenHeight - 156)

        searchContentAdapter = SearchContentAdapter(searchContentList, applicationContext, openRecipeListener)
        searchActivityContentList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        searchActivityContentList.adapter = searchContentAdapter

        filterCategoryAdapter = SearchFilterAdapter(filterCategoriesList)
        searchDishTypeList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        searchDishTypeList.adapter = filterCategoryAdapter

        filterKitchenAdapter = SearchFilterAdapter(filterKitchenList)
        searchKitchenList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        searchKitchenList.adapter = filterKitchenAdapter

        searchIngredientsAdapter = SearchIngredientsAdapter(searchIngredientsList, onIngredientClickListener)
        searchIngredientsRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        searchIngredientsRecyclerView.adapter = searchIngredientsAdapter

        searchIngredientsCounter.text = ingredientsCounter.toString()

        searchCaloriesRangeSlider.setLabelFormatter { value: Float ->
            return@setLabelFormatter "${value.roundToInt()}Cal"
        }

        searchCaloriesRangeSlider.addOnChangeListener { slider, _, _ ->
            caloriesValueFrom = slider.values.first().toInt()
            caloriesValueTo = slider.values.last().toInt()

            if (caloriesValueFrom == caloriesValueTo) {
                searchCaloriesValue.text = "$caloriesValueFrom"
            } else {
                searchCaloriesValue.text = "от $caloriesValueFrom до $caloriesValueTo"
            }
        }

        searchTimeRangeSlider.addOnChangeListener { slider, _, _ ->
            timeValueFrom = slider.values.first().toInt()
            timeValueTo = slider.values.last().toInt()

            if (timeValueFrom == timeValueTo) {
                searchTimeValue.text = "$timeValueFrom минут"
            } else {
                searchTimeValue.text = "от $timeValueFrom до $timeValueTo минут"
            }
        }

        searchIngredients.setOnClickListener {

            if (searchIngredientsContent.visibility == View.GONE) {
                searchActivityContentList.visibility = View.GONE
                searchNotFound.visibility = View.GONE
                searchIngredientsContent.visibility = View.VISIBLE
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        searchIngredientCloseButton.setOnClickListener {
            searchIngredientsContent.visibility = View.GONE
            searchActivityContentList.visibility = View.VISIBLE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_filter) {
            bottomSheetBehavior.state = when (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                true -> BottomSheetBehavior.STATE_COLLAPSED
                false -> BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSearchListFromServer(searchText: String): List<Recipe> {
        //TODO: добавить параметр "Категории"
        //TODO: прикрутить API
        if (searchText.isEmpty()) {
            return emptyList()
        } else {
            return searchContentList
        }
    }

    private fun getAllCategoriesFromServer() {
        ApiService.getApi()
                .getAllCategories()
                .enqueue(object : Callback<List<Category>> {
                    override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {

                    }

                    override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun onChangeSheetHeight(pixels: Int) {
        searchBottomSheet.layoutParams.height = pixels
    }
}