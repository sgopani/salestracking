package com.example.salestracking

import com.example.salestracking.databse.model.CartItem
import com.example.salestracking.databse.model.Collections
import com.example.salestracking.databse.model.Party
import com.example.salestracking.databse.model.Products


interface ItemClickListener {
    fun onPartyItemClick(party:Party)
    fun onOrderPartyClick(party:Party)
    //

}
interface CollectionItemClickListener{
    fun onCollectionItemClick(collection: Collections)
}
interface AddToCartItemClickListener{
    fun onProductItemClick(products: Products)
    fun deleteCartItem(cartItem: CartItem)
    fun changeQuantity(cartItem: CartItem,quantity:Int)
}