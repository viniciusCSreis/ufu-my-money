class TransactionResponse {
  final String id;
  final String value;
  final String type;
  final String description;
  final DateTime data;

  TransactionResponse({
    this.id,
    this.value,
    this.description,
    this.data,
    this.type
  });

  factory TransactionResponse.fromJson(Map<String, dynamic> json) {
    return TransactionResponse(
      id: json["id"],
      value: json["value"].toString(),
      description: json["description"],
      data: DateTime.parse(json["data"]),
      type: json["type"]
    );
  }
}
