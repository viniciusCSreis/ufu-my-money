import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_app/const.dart';
import 'package:flutter_app/createUserView.dart';
import 'package:flutter_app/listGoalsView.dart';
import 'package:flutter_app/transactionListerView.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'loginView.dart';

class MainMenuView extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    List<ListTile> listTiles = List();
    listTiles.add(
        ListTile(
          title: Text("Metas"),
          onTap: () {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => ListGoalsView()
                )
            );
          },
        )
    );
    listTiles.add(
        ListTile(
          title: Text("Transações"),
          onTap: () {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => TransactionListerView()
                )
            );
          },
        )
    );
    listTiles.add(
        ListTile(
          title: Text("Logout"),
          onTap: () async {
            SharedPreferences prefs = await SharedPreferences.getInstance();
            prefs.setString(LOGIN_TOKEN_KEY, "");

            Navigator.pushReplacement(
                context, MaterialPageRoute(builder: (context) => LoginView()));
          },
        )
    );


    return Scaffold(
        appBar: AppBar(iconTheme: IconThemeData(color: Colors.purpleAccent),title: Text("My Money")),
        body: Center(
            child: ListView.builder(
                itemCount: listTiles.length,
                itemBuilder: (context, index) => listTiles.elementAt(index)
            )
        )
    );
  }
}
