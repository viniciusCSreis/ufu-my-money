import 'dart:convert';

import 'package:flutter_app/transactionRequest.dart';
import 'package:flutter_app/transactionResponse.dart';
import 'package:http/http.dart' as http;

enum TransactionType { Receita, Despesa }


class TransactionFetcher {
  static const ServerUrl = "http://10.0.2.2:8080";

  Future<List<TransactionResponse>> list() async {
    final response = await http.get("$ServerUrl/transactions");

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
    final response = await http.delete("$ServerUrl/transactions/$id");
    if (response.statusCode == 204) {
    } else {
      throw Exception('Failed to delete transactions with id:$id');
    }
  }

  create(TransactionRequest request) async {
    final response = await http.post("$ServerUrl/transactions",
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(request.toJson()));
    if (response.statusCode == 201) {
    } else {
      throw Exception('Failed to create transactions');
    }
  }

  update(TransactionRequest request, String id) async {
    final response = await http.put("$ServerUrl/transactions/$id",
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(request.toJson()));
    if (response.statusCode == 200) {
    } else {
      throw Exception('Failed to create transactions');
    }
  }
}
