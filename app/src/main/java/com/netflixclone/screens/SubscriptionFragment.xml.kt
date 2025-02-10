package com.netflixclone.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.google.androidbrowserhelper.playbilling.provider.PaymentActivity
import com.netflixclone.R

class SubscriptionFragment : Fragment() {

    private var selectedPlan: String = "Basic"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subscription, container, false)

        val planGroup: RadioGroup = view.findViewById(R.id.planGroup)
        val btnContinue: Button = view.findViewById(R.id.btnContinue)

        planGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedPlan = when (checkedId) {
                R.id.planBasic -> "Basic"
                R.id.planStandard -> "Standard"
                R.id.planPremium -> "Premium"
                else -> "Basic"
            }
        }

        btnContinue.setOnClickListener {
            val intent = Intent(activity, PaymentActivity::class.java)
            intent.putExtra("selectedPlan", selectedPlan)
            startActivity(intent)
        }

        return view
    }
}
