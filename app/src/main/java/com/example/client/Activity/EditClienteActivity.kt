package com.example.client.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.client.DatabaseHelper.DatabaseHelperCliente
import com.example.client.R
import com.example.client.Model.Cliente

class EditClienteActivity: AppCompatActivity()
{
    private lateinit var databaseHelperCliente: DatabaseHelperCliente
    private lateinit var etNome: EditText
    private lateinit var etMail: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnDeletar: Button
    private var clienteID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_cliente_activity)

        databaseHelperCliente = DatabaseHelperCliente(this)
        etNome = findViewById(R.id.etNome)
        etMail = findViewById(R.id.etMail)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnDeletar = findViewById(R.id.btnDeletar)

        //receber o ID do cliente da Intent
        clienteID = intent.getIntExtra("CLIENTE_ID", 0)

        //carregar os dados do cliente no formulario
        carregarDadosCliente(clienteID)

        btnSalvar.setOnClickListener {
            salvarAlteracoes()
        }

        btnDeletar.setOnClickListener{
            mostrarConfirmacaoDelecao()
        }
    }

        private fun carregarDadosCliente(id: Int)
    {
        val cliente = databaseHelperCliente.obterClientePorID(id)

        if(cliente != null)
        {
            etNome.setText(cliente.nome)
            etMail.setText(cliente.email)
        }
    }

    private fun salvarAlteracoes()
    {
        val nome = etNome.text.toString()
        val email = etMail.text.toString()

        if(nome.isNotEmpty() && email.isNotEmpty())
        {
            val cliente = Cliente(id = clienteID, nome = nome, email = email)

            databaseHelperCliente.atualizarClient(cliente)

            Toast.makeText(this, "Cliente Atualizado!", Toast.LENGTH_SHORT).show()

            finish() // fecha a atividade e volta para a MainActitvity
        }
        else
        {
            Toast.makeText(this, "Por Favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarConfirmacaoDelecao()
    {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Deletar Cliente")
        builder.setMessage("Tem certeza de que deseja deletar este cliente?")
        builder.setPositiveButton("Sim") { dialog, which ->
            deletarCliente()
        }

        builder.setNegativeButton("Não") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun deletarCliente()
    {
        val result = databaseHelperCliente.deletarCliente(clienteID)

        if(result > 0)
        {
            Toast.makeText(this, "Cliente deletado com sucesso!", Toast.LENGTH_SHORT).show()

            finish()
        }
        else
        {
            Toast.makeText(this, "Erro ao deletar o cliente.", Toast.LENGTH_SHORT).show()
        }
    }
}