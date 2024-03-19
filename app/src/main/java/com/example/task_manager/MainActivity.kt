package com.example.task_manager

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.navArgument
import com.example.task_manager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Lista para armazenar as tarefas
        val taskList = mutableListOf<Tasks>()
        val title = ""
        var description = ""
        var isDone = false

        fun createDescriptionPopUp() {
            // Cria o PopUp de descrição da tarefa
            val popUpDescription = AlertDialog.Builder(this)
            popUpDescription.setTitle("Descrição:")

            val titleDescription = EditText(this)
            popUpDescription.setView(titleDescription)

            popUpDescription.setPositiveButton("Ok") { dialog, _ ->
                description = titleDescription.text.toString()
                // Cria a task somente após preencher ambas as pop-ups
                val task = Tasks(title, description, isDone)
                taskList.add(task)
                Snackbar.make(binding.fab, task.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            popUpDescription.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

            popUpDescription.show()
        }

        fun createStatusPopUp() {
            val statusOptions = arrayOf("Pendente", "Concluída")

            // Cria o PopUp
            val popUpStatus = AlertDialog.Builder(this)
            popUpStatus.setTitle("Status:")

            // Cria um Spinner (dropdown) para escolher o status da tarefa
            val spinner = Spinner(this)
            spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusOptions)
            popUpStatus.setView(spinner)

            popUpStatus.setPositiveButton("Ok") { dialog, _ ->
                val selectedStatus = spinner.selectedItem as String
                isDone =  selectedStatus == "Concluída"
                createDescriptionPopUp()
            }
            popUpStatus.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

            popUpStatus.show()
        }

        fun createTitlePopUp() {
            // Cria o PopUp
            val popUpTitle = AlertDialog.Builder(this)
            popUpTitle.setTitle("Título:")

            // Cria uma caixa de texto
            val titleText = EditText(this)
            popUpTitle.setView(titleText)

            popUpTitle.setPositiveButton("Ok") { dialog, _ ->
                // Se o título for preenchido, exibe a pop-up de descrição
                if (titleText.text.isNotEmpty()) {
                    createStatusPopUp()
                } else {
                    // Caso contrário, mostra um aviso
                    Toast.makeText(this, "O título não pode estar vazio", Toast.LENGTH_SHORT).show()
                }
            }
            popUpTitle.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

            popUpTitle.show()
        }

        binding.fab.setOnClickListener { view ->
            createTitlePopUp()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}