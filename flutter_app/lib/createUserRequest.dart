class CreateUserRequest {
  final String cpf;
  final String username;
  final String email;
  final String password;
  final String phoneNumber;
  final String birthDate;

  CreateUserRequest({
    this.cpf,
    this.username,
    this.email,
    this.password,
    this.phoneNumber,
    this.birthDate,
  });

  Map<String, dynamic> toJson() => {
        'cpf': this.cpf,
        'email': this.email,
        'phoneNumber': this.phoneNumber,
        'birthDate': this.birthDate,
        'username': this.username,
        'password': this.password,
      };
}
