import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_app/listGoalsView.dart';
import 'package:flutter_app/transactionListerView.dart';

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
