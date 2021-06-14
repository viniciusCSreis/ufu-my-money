import 'dart:core';
import 'dart:convert';
import 'package:flutter_app/goalsRequest.dart';
import 'package:flutter_app/goalsResponse.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import 'const.dart';

class GoalsFetcher {
  static const ServerUrl = "http://10.0.2.2:8080";

  Future<List<GoalsResponse>> list() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var loginToken = prefs.getString(LOGIN_TOKEN_KEY);

    final response =
        await http.get("$ServerUrl/goals", headers: <String, String>{
      LOGIN_TOKEN_HEADER: loginToken,
    });

    if (response.statusCode == 200) {
      Iterable jsonList = jsonDecode(response.body);
      return jsonList.map((json) => GoalsResponse.fromJson(json)).toList();
    } else {
      throw Exception('Failed to list goals');
    }
  }

  delete(String id) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var loginToken = prefs.getString(LOGIN_TOKEN_KEY);

    final response =
        await http.delete("$ServerUrl/goals/$id", headers: <String, String>{
      LOGIN_TOKEN_HEADER: loginToken,
    });

    if (response.statusCode == 204) {
    } else {
      throw Exception('Failed to delete goal with id:$id');
    }
  }

  create(GoalsRequest request) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var loginToken = prefs.getString(LOGIN_TOKEN_KEY);

    final response = await http.post("$ServerUrl/goals",
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          LOGIN_TOKEN_HEADER: loginToken,
        },
        body: jsonEncode(request.toJson()));
    if (response.statusCode == 201) {
    } else {
      throw Exception('Failed to create goal');
    }
  }

  update(GoalsRequest request, String id) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var loginToken = prefs.getString(LOGIN_TOKEN_KEY);

    final response = await http.put("$ServerUrl/goals/$id",
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          LOGIN_TOKEN_HEADER: loginToken,
        },
        body: jsonEncode(request.toJson()));
    if (response.statusCode == 200) {
    } else {
      throw Exception('Failed to create goal');
    }
  }
}
