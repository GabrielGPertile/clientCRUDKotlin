package com.example.client.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.client.DatabaseHelper.DatabaseHelperCliente
import com.example.client.R
import com.example.client.Model.Cliente

class MainActivity : AppCompatActivity()
{
    private lateinit var dataBaseHelperCliente: DatabaseHelperCliente
    private lateinit var etNome: EditText
    private lateinit var etMail: EditText
    private lateinit var btnAdicionar: Button
    private lateinit var listViewClientes: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var listaCliente = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        //Inicializa os componentes
        dataBaseHelperCliente = DatabaseHelperCliente(this)
            etNome = (R.id.etNome)
        etMail = findViewById(R.id.etMail)
        btnAdicionar = findViewById(R.id.btnAdicionar)
        listViewClientes = findViewById(R.id.listViewClientes)

        //configura o botão para adicionar clientes
        btnAdicionar.setOnClickListener {
            adicionarCliente()
        }

        carregarClientes()

        listViewClientes.setOnItemClickListener{ parent, view, position, id ->
            val clienteSelecionado = dataBaseHelperCliente.obterClientes()[position]
            val intent = Intent(this, EditClienteActivity::class.java)
            intent.putExtra("CLIENTE_ID", clienteSelecionado.id)
            startActivity(intent)
        }
    }

    override fun onResume()
    {
        super.onResume()

        //Recarregar clientes quando a atividade é retomada
        carregarClientes()
    }

    private fun adicionarCliente()
    {
        val nome = etNome.text.toString()
        val email = etMail.text.toString()

        if(nome.isNotEmpty() && email.isNotEmpty())
        {
            val cliente = Cliente(nome = nome, email = email)
            dataBaseHelperCliente.adicionarCliente(cliente)
            Toast.makeText(this, "Cliente adicionado!", Toast.LENGTH_SHORT).show()
            etNome.text.clear()
            etMail.text.clear()
            carregarClientes()
        }
        else
        {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregarClientes()
    {
        listaCliente.clear()

        val clientes = dataBaseHelperCliente.obterClientes()

        for(cliente in clientes)
        {
            listaCliente.add("ID: ${cliente.id}, Nome: ${cliente.nome}, Email: ${cliente.email}")
        }

            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaCliente)

        listViewClientes.adapter = adapter;
    }
}