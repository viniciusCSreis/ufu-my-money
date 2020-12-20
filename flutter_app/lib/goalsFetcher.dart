import 'dart:core';
import 'dart:convert';
import 'package:flutter_app/goalsRequest.dart';
import 'package:flutter_app/goalsResponse.dart';
import 'package:http/http.dart' as http;

class GoalsFetcher {
  static const ServerUrl = "http://10.0.2.2:8080";

  Future<List<GoalsResponse>> list() async {
    final response = await http.get("$ServerUrl/goals");

    if (response.statusCode == 200) {
      Iterable jsonList = jsonDecode(response.body);
      return jsonList.map((json) => GoalsResponse.fromJson(json)).toList();
    } else {
      throw Exception('Failed to list goals');
    }
  }

  delete(String id) async {
    final response = await http.delete("$ServerUrl/goals/$id");
    if (response.statusCode == 204) {
    } else {
      throw Exception('Failed to delete goal with id:$id');
    }
  }

  create(GoalsRequest request) async {
    final response = await http.post("$ServerUrl/goals",
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(request.toJson()));
    if (response.statusCode == 201) {
    } else {
      throw Exception('Failed to create goal');
    }
  }

  update(GoalsRequest request, String id) async {
    var d = request.description;
    print("request.d=$d");
    final response = await http.put("$ServerUrl/goals/$id",
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(request.toJson()));
    if (response.statusCode == 200) {
    } else {
      throw Exception('Failed to create goal');
    }
  }
}
