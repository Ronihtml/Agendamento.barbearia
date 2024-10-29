package com.example.aplicativosorteio

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var spinnerService: Spinner
    private lateinit var calendarView: CalendarView
    private lateinit var timeSlotContainer: LinearLayout
    private lateinit var btnSchedule: Button

    private var selectedTime: String? = null
    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupSpinner()
        setupCalendar()
        setupTimeSlots()
        setupScheduleButton()
    }

    private fun initializeViews() {
        spinnerService = findViewById(R.id.spinnerService)
        calendarView = findViewById(R.id.calendarView)
        timeSlotContainer = findViewById(R.id.timeSlotContainer)
        btnSchedule = findViewById(R.id.btnSchedule)
    }

    private fun setupSpinner() {
        val services = arrayOf(
            "Corte de Cabelo - R$ 50,00",
            "Barba - R$ 30,00",
            "Corte + Barba - R$ 70,00"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, services)
        spinnerService.adapter = adapter
    }

    private fun setupCalendar() {
        calendarView.minDate = System.currentTimeMillis()
        calendarView.setOnDateChangeListener { _, year, month, day ->
            selectedDate.set(year, month, day)
            updateTimeSlots()
        }
    }

    private fun updateTimeSlots() {
        TODO("Not yet implemented")
    }

    private fun setupTimeSlots() {
        val timeSlots = arrayOf(
            "09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00"
        )

        timeSlotContainer.removeAllViews()
        timeSlots.forEach { time ->
            val timeButton = Button(this).apply {
                text = time
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 8.dpToPx()
                }
                setTextColor(ContextCompat.getColor(context, R.color.black))
                background = ContextCompat.getDrawable(context, R.drawable.time_slot_background)

                setOnClickListener {
                    selectedTime = time
                    updateTimeSlotSelection()
                }
            }
            timeSlotContainer.addView(timeButton)
        }
    }

    private fun updateTimeSlotSelection() {
        for (i in 0 until timeSlotContainer.childCount) {
            val button = timeSlotContainer.getChildAt(i) as Button
            if (button.text == selectedTime) {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.brown_primary))
                button.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                button.background = ContextCompat.getDrawable(this, R.drawable.time_slot_background)
                button.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }
    }

    private fun setupScheduleButton() {
        btnSchedule.setOnClickListener {
            if (selectedTime == null) {
                showMessage("Por favor, selecione um horário")
                return@setOnClickListener
            }

            val service = spinnerService.selectedItem.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = dateFormat.format(selectedDate.time)

            showConfirmationDialog(service, date, selectedTime!!)
        }
    }

    private fun showConfirmationDialog(service: String, date: String, time: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Agendamento")
            .setMessage("Serviço: $service\nData: $date\nHorário: $time")
            .setPositiveButton("Confirmar") { _, _ ->
                saveAppointment(service, date, time)
                showSuccessMessage()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun saveAppointment(service: String, date: String, time: String) {
        // Implementar a lógica de salvamento do agendamento
    }

    private fun showSuccessMessage() {
        AlertDialog.Builder(this)
            .setTitle("Sucesso!")
            .setMessage("Seu agendamento foi realizado com sucesso!")
            .setPositiveButton("OK") { _, _ ->
                finish()
            }
            .show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
