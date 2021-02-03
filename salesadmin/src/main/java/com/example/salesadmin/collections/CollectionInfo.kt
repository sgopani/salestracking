package com.example.salesadmin.collections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.salesadmin.R
import com.example.salesadmin.leave.LeaveInfoArgs
import com.example.salesadmin.repository.FireStoreViewModel
import org.w3c.dom.Text

class CollectionInfo : Fragment() {
    private lateinit var rootView: View
    private lateinit var partyName:TextView
    private lateinit var employeeName:TextView
    private lateinit var date:TextView
    private lateinit var collectionType:TextView
    private lateinit var note:TextView
    private lateinit var viewModel: FireStoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init(){
        partyName=rootView.findViewById(R.id.tv_party_name_info)
        employeeName=rootView.findViewById(R.id.tv_employee_name_info)
        date=rootView.findViewById(R.id.tv_date_info)
        collectionType=rootView.findViewById(R.id.tv_collection_type_info)
        note=rootView.findViewById(R.id.tv_note_info)
        viewModel= FireStoreViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView=inflater.inflate(R.layout.collection_info, container, false)
        init()
        val collectionInfo = CollectionInfoArgs.fromBundle(requireArguments()).collections
        partyName.text=collectionInfo.partyName
        employeeName.text=collectionInfo.employeeName
        date.text=collectionInfo.date
        collectionType.text=collectionInfo.collectionType
        note.text=collectionInfo.collectionNote
        viewModel.selectedCollection.observe(this.requireActivity(), Observer {collection ->
            partyName.text=collection.partyName
            employeeName.text=collection.employeeName
            date.text=collection.date
            collectionType.text=collection.collectionType
            note.text=collection.collectionNote
        })
        return rootView
    }

}