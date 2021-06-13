import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_app/listGoalsView.dart';
import 'package:flutter_app/transactionListerView.dart';

class LoginView extends StatefulWidget {
  @override
  _LoginViewState createState() => _LoginViewState();
}

class _LoginViewState extends State<LoginView> {
  final _form = GlobalKey<FormState>();

  // Initially password is obscure
  bool _obscureText = true;

  String _password;

  @override
  Widget build(BuildContext context) {
    // Toggles the password show status
    void _toggle() {
      setState(() {
        _obscureText = !_obscureText;
      });
    }

    return Scaffold(
      appBar: AppBar(title: Text("My Money Login")),
      body: Center(
          child: Padding(
        padding: EdgeInsets.all(15),
        child: Form(
          key: _form,
          child: Column(
            children: <Widget>[
              TextFormField(
                initialValue: "",
                decoration: InputDecoration(labelText: "Username"),
                onSaved: (value) => print("ola"),
                keyboardType: TextInputType.text,
              ),
              TextFormField(
                initialValue: "",
                decoration: InputDecoration(labelText: "Password"),
                onSaved: (value) => print("ola"),
                obscureText: true,
              ),
              
            ],
          ),
        ),
      )),
    );
  }
}
