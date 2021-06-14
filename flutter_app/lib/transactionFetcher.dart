import 'dart:convert';

import 'package:flutter_app/transactionRequest.dart';
import 'package:flutter_app/transactionResponse.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import 'const.dart';

enum TransactionType { Receita, Despesa }

class TransactionFetcher {
  static const ServerUrl = "http://10.0.2.2:8080";

  Future<List<TransactionResponse>> list() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var loginToken = prefs.getString(LOGIN_TOKEN_KEY);

    final response =
        await http.get("$ServerUrl/transactions", headers: <String, String>{
      LOGIN_TOKEN_HEADER: loginToken,
    });

    if (response.statusCode == 200) {
      Iterable jsonList = jsonDecode(response.body);
      return jsonList
          .map((json) => TransactionResponse.fromJson(json))
          .toList();
    } else {
      throw Exception('Failed to list transaction');
    }
  }

  delete(String id) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var loginToken = prefs.getString(LOGIN_TOKEN_KEY);

    final response = await http
        .delete("$ServerUrl/transactions/$id", headers: <String, String>{
      LOGIN_TOKEN_HEADER: loginToken,
    });
    if (response.statusCode == 204) {
    } else {
      throw Exception('Failed to delete transactions with id:$id');
    }
  }

  create(TransactionRequest request) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var loginToken = prefs.getString(LOGIN_TOKEN_KEY);

    final response = await http.post("$ServerUrl/transactions",
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          LOGIN_TOKEN_HEADER: loginToken,
        },
        body: jsonEncode(request.toJson()));
    if (response.statusCode == 201) {
    } else {
      throw Exception('Failed to create transactions');
    }
  }

  update(TransactionRequest request, String id) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var loginToken = prefs.getString(LOGIN_TOKEN_KEY);

    final response = await http.put("$ServerUrl/transactions/$id",
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          LOGIN_TOKEN_HEADER: loginToken,
        },
        body: jsonEncode(request.toJson()));
    if (response.statusCode == 200) {
    } else {
      throw Exception('Failed to create transactions');
    }
  }
}
