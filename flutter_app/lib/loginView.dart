import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_app/createUserView.dart';
import 'package:flutter_app/listGoalsView.dart';
import 'package:flutter_app/loginRequest.dart';
import 'package:flutter_app/loginResponse.dart';
import 'package:flutter_app/mainMenuView.dart';
import 'package:flutter_app/transactionListerView.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'const.dart';
import 'loginFetcher.dart';

class LoginView extends StatefulWidget {
  @override
  _LoginViewState createState() => _LoginViewState();
}

class _LoginViewState extends State<LoginView> {
  final _form = GlobalKey<FormState>();
  var fetcher = LoginFetcher();
  Map<String, Object> _formValues = {};

  @override
  void initState() {
    super.initState();

    // it will navigate to login page as soon as this state is built
    Timer.run(() async {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      var loginToken = prefs.getString(LOGIN_TOKEN_KEY);
      if (loginToken != "") {
        print("loginToken:" + loginToken);
        Navigator.pushReplacement(
            context, MaterialPageRoute(builder: (context) => MainMenuView()));
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomPadding: false,
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
                onSaved: (value) => _formValues["username"] = value,
                keyboardType: TextInputType.text,
              ),
              TextFormField(
                initialValue: "",
                decoration: InputDecoration(labelText: "Password"),
                onSaved: (value) => _formValues["password"] = value,
                obscureText: true,
              ),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 16.0),
                child: ElevatedButton(
                  onPressed: () async {
                    _form.currentState.save();

                    var loginRequest = LoginRequest(
                      username: _formValues["username"],
                      password: _formValues["password"],
                    );

                    LoginResponse response = await fetcher.login(loginRequest);

                    await createLoginFile(response);
                    SharedPreferences prefs =
                        await SharedPreferences.getInstance();
                    print("loginToken:" + prefs.getString(LOGIN_TOKEN_KEY));

                    Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(
                            builder: (context) => MainMenuView()));
                  },
                  child: Text('Login'),
                ),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 150.0),
                child: ElevatedButton(
                  onPressed: () async {
                    Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) => CreateUserView()));
                  },
                  child: Text('Registrar'),
                ),
              ),
            ],
          ),
        ),
      )),
    );
  }

  createLoginFile(LoginResponse response) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setString(LOGIN_TOKEN_KEY, response.token);
  }
}
