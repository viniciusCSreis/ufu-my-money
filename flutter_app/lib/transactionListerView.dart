import 'package:flutter/material.dart';
import 'package:flutter_app/goalsManagerView.dart';
import 'package:flutter_app/transactionFetcher.dart';
import 'package:flutter_app/transactionModifierView.dart';
import 'package:flutter_app/transactionResponse.dart';
import 'package:intl/intl.dart';

class TransactionListerView extends StatefulWidget {
  @override
  _TransactionListerViewState createState() => _TransactionListerViewState();
}

class _TransactionListerViewState extends State<TransactionListerView> {
  Future<List<TransactionResponse>> transactionsResponse;
  DateFormat dateFormat = DateFormat('dd-MM-yy');
  var fetcher = TransactionFetcher();

  @override
  void initState() {
    transactionsResponse = fetcher.list();
    super.initState();
  }

  @override
  void setState(fn) {
    transactionsResponse = fetcher.list();
    super.setState(fn);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Transações"),
        actions: [
          IconButton(
            icon: Icon(Icons.add),
            onPressed: () => Navigator.push(
              context,
              MaterialPageRoute(builder: (context) => TransactionModifierView(null)),
            ).then((value) {
              setState(() {});
            }),
          ),
        ],
      ),
      body: Center(
        child: FutureBuilder<List<TransactionResponse>>(
            future: transactionsResponse,
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                return ListView.builder(
                    itemCount: snapshot.data.length,
                    itemBuilder: (context, index) {
                      TransactionResponse transaction = snapshot.data.elementAt(index);
                      var textColor = Colors.green;
                      if ( transaction.type == TransactionType.Despesa.toString().split('.').last) {
                        textColor = Colors.red;
                      }
                      return ListTile(
                        title: Text(
                            "R\$ " + transaction.value.toString(),
                            style: TextStyle(color: textColor)
                        ),
                        subtitle: Text(
                            dateFormat.format(transaction.data) + "\n" + transaction.description,
                            style: TextStyle(color: textColor)
                        ),
                        trailing: Container(
                          width: 100,
                          child: Row(
                            children: <Widget>[
                              IconButton(
                                icon: Icon(Icons.edit),
                                onPressed: () => Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (context) =>
                                            TransactionModifierView(transaction)))
                                    .then((value) {
                                  setState(() {});
                                }),
                              ),
                              IconButton(
                                  icon: Icon(Icons.delete),
                                  onPressed: () async {
                                    await fetcher.delete(transaction.id);
                                    setState(() {});
                                  }),
                            ],
                          ),
                        ),
                      );
                    });
              } else if (snapshot.hasError) {
                return Text("${snapshot.error}");
              }

              return CircularProgressIndicator();
            }),
      ),
    );
  }
}
