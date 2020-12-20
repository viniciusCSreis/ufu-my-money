import 'package:flutter/material.dart';
import 'package:flutter_app/goalsFetcher.dart';
import 'package:flutter_app/goalsManagerView.dart';
import 'package:flutter_app/goalsResponse.dart';

class ListGoalsView extends StatefulWidget {
  @override
  _ListGoalsViewState createState() => _ListGoalsViewState();
}

class _ListGoalsViewState extends State<ListGoalsView> {
  Future<List<GoalsResponse>> goalsResponse;
  var fetcher = GoalsFetcher();

  @override
  void initState() {
    goalsResponse = fetcher.list();
    super.initState();
  }

  @override
  void setState(fn) {
    goalsResponse = fetcher.list();
    super.setState(fn);
  }

  @override
  Widget build(BuildContext context) {
    print("hello world");
    return Scaffold(
      appBar: AppBar(
        title: Text("List Goals View"),
        actions: [
          IconButton(
            icon: Icon(Icons.add),
            onPressed: () => Navigator.push(
              context,
              MaterialPageRoute(builder: (context) => GoalsManagerView(null)),
            ).then((value) {
              setState(() {});
            }),
          ),
        ],
      ),
      body: Center(
        child: FutureBuilder<List<GoalsResponse>>(
            future: goalsResponse,
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                return ListView.builder(
                    itemCount: snapshot.data.length,
                    itemBuilder: (context, index) {
                      GoalsResponse goal = snapshot.data.elementAt(index);
                      return ListTile(
                        title: Text("Meta: " + goal.data.toString()),
                        subtitle: Text(goal.description),
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
                                                GoalsManagerView(goal)))
                                    .then((value) {
                                  setState(() {});
                                }),
                              ),
                              IconButton(
                                  icon: Icon(Icons.delete),
                                  onPressed: () async {
                                    await fetcher.delete(goal.id);
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
