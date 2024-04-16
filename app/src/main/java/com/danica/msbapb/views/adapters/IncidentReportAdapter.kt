package com.danica.msbapb.views.adapters


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danica.msbapb.R
import com.danica.msbapb.models.IncidentReport



interface IncidentClickListener {
    fun onClick(incidentReport: IncidentReport)
}

class IncidentReportAdapter(
    private val context: Context,
    private val incidentReports: List<IncidentReport>,
    private  val incidentClickListener: IncidentClickListener
) :
    RecyclerView.Adapter<IncidentReportAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.adapter_reports, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val incidentReport = incidentReports[position]
        holder.textLocation.text = "Location: " + incidentReport.location
        holder.textType.text = "Type: " + incidentReport.type
        holder.textDescription.text = "Description: " + incidentReport.description
        holder.textStatus.text = incidentReport.status
        holder.textIncidentDate.text = "Incident Date: " + incidentReport.incident_date
        holder.textSeverity.text = incidentReport.severity
        holder.frameSeverity.setBackgroundColor(setColor(incidentReport.severity,context))

        holder.itemView.setOnClickListener {
            incidentClickListener.onClick(incidentReport)
        }
    }

    override fun getItemCount(): Int {
        return incidentReports.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textLocation: TextView
        var textType: TextView
        var textDescription: TextView
        var textStatus: TextView
        var textIncidentDate: TextView
        var textSeverity: TextView
        var frameSeverity : FrameLayout
        init {
            textLocation = itemView.findViewById(R.id.text_location)
            textType = itemView.findViewById(R.id.text_type)
            textDescription = itemView.findViewById(R.id.text_description)
            textSeverity = itemView.findViewById(R.id.text_severity)
            textIncidentDate = itemView.findViewById(R.id.text_incident_date)
            textStatus = itemView.findViewById(R.id.text_status)
            frameSeverity = itemView.findViewById(R.id.frameSeverity)
        }
    }

    fun setColor(data: String, context: Context): Int {
        when (data) {
            "LOW" -> return Color.parseColor("#4CAF50") // Green
            "MEDIUM" -> return Color.parseColor("#FF9800") // Yellow
            "HIGH" -> return Color.parseColor("#F44336") // Red
            else -> return Color.parseColor("#808080") // Gray
        }
    }




}
