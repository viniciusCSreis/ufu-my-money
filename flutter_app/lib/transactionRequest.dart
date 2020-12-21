class TransactionRequest {
  final String value;
  final String description;
  final DateTime data;
  final String type;


  TransactionRequest({
    this.value,
    this.description,
    this.data,
    this.type,
  });

  Map<String, dynamic> toJson() => {
    'value': this.value,
    'description': this.description,
    'data': this.data.toIso8601String(),
    'type': this.type,
  };
}
