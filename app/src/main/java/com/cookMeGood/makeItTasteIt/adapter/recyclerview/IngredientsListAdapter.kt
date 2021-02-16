package com.cookMeGood.makeItTasteIt.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cookMeGood.makeItTasteIt.R
import com.cookMeGood.makeItTasteIt.api.dto.Ingredient
import kotlinx.android.synthetic.main.item_ingredient.view.*

class IngredientsListAdapter(val context: Context, private var ingredients: List<Ingredient>): RecyclerView.Adapter<IngredientsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_ingredient,
                        parent, false
                )
        )
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currIngredient = ingredients[position]

        holder.name.text = currIngredient.name
        holder.amount.text = currIngredient.amount
    }

    class ViewHolder(item: View): RecyclerView.ViewHolder(item){
        var name = item.ingredientName!!
        var amount = item.ingredientAmount!!
        var chackBox = item.ingredientCheckBox!!
    }
}