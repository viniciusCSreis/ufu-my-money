import 'dart:convert';

import 'package:flutter_app/createUserRequest.dart';
import 'package:flutter_app/createUserView.dart';
import 'package:flutter_app/loginRequest.dart';
import 'package:flutter_app/transactionRequest.dart';
import 'package:flutter_app/transactionResponse.dart';
import 'package:http/http.dart' as http;

import 'loginResponse.dart';

class LoginFetcher {
  static const ServerUrl = "http://10.0.2.2:8080";

  createUser(CreateUserRequest request) async {
    final response = await http.post("$ServerUrl/sign-up",
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(request.toJson()));
    if (response.statusCode == 201) {
    } else {
      print("response.statusCode:" + response.statusCode.toString());
      throw Exception("Fail to create user: " + response.body.toString());
    }
  }

  Future<LoginResponse> login(LoginRequest request) async {
    final response = await http.post("$ServerUrl/sign-in",
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(request.toJson()));
    if (response.statusCode == 201) {
      Map<String, dynamic> json = jsonDecode(response.body);
      return LoginResponse.fromJson(json);
    } else {
      print("response.statusCode:" + response.statusCode.toString());
      throw Exception("Fail to make login: " + response.body.toString());
    }
  }
}
