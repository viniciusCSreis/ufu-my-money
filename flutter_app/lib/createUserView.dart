import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_app/createUserRequest.dart';
import 'package:flutter_app/listGoalsView.dart';
import 'package:flutter_app/loginRequest.dart';
import 'package:flutter_app/loginResponse.dart';
import 'package:flutter_app/loginView.dart';
import 'package:flutter_app/transactionListerView.dart';
import 'package:intl/intl.dart';

import 'loginFetcher.dart';

class CreateUserView extends StatefulWidget {
  @override
  _CreateUserViewState createState() => _CreateUserViewState();
}

class _CreateUserViewState extends State<CreateUserView> {
  final _form = GlobalKey<FormState>();
  var fetcher = LoginFetcher();
  Map<String, Object> _formValues = {};

  DateFormat dateFormat = DateFormat('dd-MM-yy');
  TextEditingController _textEditingController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomPadding: false,
      appBar: AppBar(title: Text("My Money Registro")),
      body: Center(
          child: Padding(
        padding: EdgeInsets.all(15),
        child: Form(
          key: _form,
          child: Column(
            children: <Widget>[
              TextFormField(
                initialValue: "",
                decoration: InputDecoration(labelText: "CPF"),
                onSaved: (value) => _formValues["cpf"] = value,
              ),
              TextField(
                decoration: InputDecoration(labelText: "Data de Nascimento"),
                focusNode: AlwaysDisabledFocusNode(),
                controller: _textEditingController,
                onTap: () => _selectDate(context),
              ),
              TextFormField(
                initialValue: "",
                decoration: InputDecoration(labelText: "Username"),
                onSaved: (value) => _formValues["username"] = value,
                keyboardType: TextInputType.text,
              ),
              TextFormField(
                initialValue: "",
                decoration: InputDecoration(labelText: "Password"),
                onSaved: (value) => _formValues["password"] = value,
                obscureText: true,
              ),
              TextFormField(
                initialValue: "",
                decoration: InputDecoration(labelText: "Email"),
                onSaved: (value) => _formValues["email"] = value,
                keyboardType: TextInputType.emailAddress,
              ),
              TextFormField(
                initialValue: "",
                decoration: InputDecoration(labelText: "Número de Telefone"),
                onSaved: (value) => _formValues["phone"] = value,
                keyboardType: TextInputType.phone,
              ),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 16.0),
                child: ElevatedButton(
                  onPressed: () async {
                    _form.currentState.save();

                    var request = CreateUserRequest(
                      cpf: _formValues["cpf"],
                      birthDate: _formValues["birthDate"],
                      email: _formValues["email"],
                      phoneNumber: _formValues["phoneNumber"],
                      username: _formValues["username"],
                      password: _formValues["password"],
                    );

                    try{
                      await fetcher.createUser(request);
                    }catch(error){
                      return showDialog<String>(
                        context: context,
                        builder: (BuildContext context) => AlertDialog(
                          title: const Text('Falha ao criar usuario'),
                          content: const Text('Usuário já existe.'),
                          actions: <Widget>[
                            TextButton(
                              onPressed: () => Navigator.pop(context, 'OK'),
                              child: const Text('OK'),
                            ),
                          ],
                        ),
                      );
                    }

                    Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(
                            builder: (context) => LoginView()));
                  },
                  child: Text('Criar Usuário'),
                ),
              ),
            ],
          ),
        ),
      )),
    );
  }

  _selectDate(BuildContext context) async {
    DateTime newSelectedDate = await showDatePicker(
      context: context,
      initialDate: DateTime(2000),
      firstDate: DateTime(1900),
      lastDate: DateTime(2021),
    );

    if (newSelectedDate != null) {
      _formValues["data"] = newSelectedDate;
      _textEditingController
        ..text = dateFormat.format(newSelectedDate)
        ..selection = TextSelection.fromPosition(TextPosition(
            offset: _textEditingController.text.length,
            affinity: TextAffinity.upstream));
    }
  }
}

class AlwaysDisabledFocusNode extends FocusNode {
  @override
  bool get hasFocus => false;
}
