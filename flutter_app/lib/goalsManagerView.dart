import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_app/goalsRequest.dart';

import 'goalsFetcher.dart';
import 'goalsResponse.dart';

class GoalsManagerView extends StatefulWidget {
  final GoalsResponse goalsResponse;

  GoalsManagerView(this.goalsResponse);

  @override
  _GoalsManagerViewState createState() => _GoalsManagerViewState();
}

class _GoalsManagerViewState extends State<GoalsManagerView> {
  final _form = GlobalKey<FormState>();
  var fetcher = GoalsFetcher();

  DateTime _selectedDate;
  TextEditingController _textEditingController = TextEditingController();
  Map<String, Object> _formValues = {};

  @override
  void initState() {
    if (widget.goalsResponse != null) {
      _formValues["id"] = widget.goalsResponse.id;
      _formValues["data"] = widget.goalsResponse.data;
      _formValues["description"] = widget.goalsResponse.description;
      _formValues["value"] = widget.goalsResponse.value;
      _textEditingController..text = _formValues["data"].toString();
    }

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Goals Manager View"),
        actions: [
          IconButton(
              icon: Icon(Icons.save),
              onPressed: () async {
                _form.currentState.save();

                var goalsRequest = GoalsRequest(
                  data: _formValues["data"],
                  description: _formValues["description"],
                  value: _formValues["value"],
                );

                String id = _formValues["id"];
                print("id=$id");
                if (id == null) {
                  await fetcher.create(goalsRequest);
                } else {
                  await fetcher.update(goalsRequest, id);
                }

                Navigator.of(context).pop();
              }),
        ],
      ),
      body: Center(
          child: Padding(
        padding: EdgeInsets.all(15),
        child: Form(
          key: _form,
          child: Column(
            children: <Widget>[
              TextField(
                decoration: InputDecoration(labelText: "Dia"),
                focusNode: AlwaysDisabledFocusNode(),
                controller: _textEditingController,
                onTap: () => _selectDate(context),
              ),
              TextFormField(
                initialValue: _formValues["description"],
                decoration: InputDecoration(labelText: "Descrição"),
                onSaved: (value) => _formValues["description"] = value,
              ),
              TextFormField(
                initialValue: _formValues["value"],
                decoration: InputDecoration(labelText: "Valor"),
                onSaved: (value) => _formValues["value"] = value,
                keyboardType: TextInputType.number,
              ),
            ],
          ),
        ),
      )),
    );
  }

  _selectDate(BuildContext context) async {
    DateTime newSelectedDate = await showDatePicker(
      context: context,
      initialDate: _selectedDate != null ? _selectedDate : DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2100),
    );

    if (newSelectedDate != null) {
      _formValues["data"] = newSelectedDate;
      _textEditingController
        ..text = newSelectedDate.toString()
        ..selection = TextSelection.fromPosition(TextPosition(
            offset: _textEditingController.text.length,
            affinity: TextAffinity.upstream));
    }
  }
}

class AlwaysDisabledFocusNode extends FocusNode {
  @override
  bool get hasFocus => false;
}
