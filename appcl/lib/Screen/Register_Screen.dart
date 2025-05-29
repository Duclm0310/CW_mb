import 'package:appcl/services/auth_service.dart';
import 'package:flutter/material.dart';

class RegisterScreen extends StatefulWidget {
  @override
  _RegisterScreenState createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  String errorMessage = "";
  final AuthService _authService = new AuthService();

  Future<void> registerUser() async {
    String name = _nameController.text.trim();
    String email = _emailController.text.trim();
    String password = _passwordController.text.trim();
    String result = await _authService.registerUser(email, password, name);

    if (result == "success") {
      Navigator.pushReplacementNamed(context, '/login');
    } else {
      setState(() {
        errorMessage = result;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Register"),
        backgroundColor: Colors.purple.shade50,
        elevation: 0,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              const SizedBox(height: 40),
              const Text(
                'Create Your Account',
                style: TextStyle(
                  fontSize: 32,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 10),
              const Text(
                'Please register to get started',
                style: TextStyle(
                  fontSize: 18,
                  color: Colors.grey,
                ),
              ),
              const SizedBox(height: 40),

              // Textfield cho Name
              TextField(
                controller: _nameController,
                decoration: InputDecoration(
                  labelText: "Full Name",
                  border: OutlineInputBorder(),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.purple.shade50),
                  ),
                  prefixIcon: Icon(Icons.person, color: Colors.purple.shade50),
                ),
              ),
              const SizedBox(height: 20),

              // Textfield cho Email
              TextField(
                controller: _emailController,
                keyboardType: TextInputType.emailAddress,
                decoration: InputDecoration(
                  labelText: "Email",
                  border: OutlineInputBorder(),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.purple.shade50),
                  ),
                  prefixIcon: Icon(Icons.email, color: Colors.purple.shade50),
                ),
              ),
              const SizedBox(height: 20),

              // Textfield cho Password
              TextField(
                controller: _passwordController,
                obscureText: true,
                decoration: InputDecoration(
                  labelText: "Password",
                  border: OutlineInputBorder(),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.purple.shade50),
                  ),
                  prefixIcon: Icon(Icons.lock, color: Colors.purple.shade50),
                ),
              ),
              const SizedBox(height: 20),

              // Nút Register
              ElevatedButton(
                onPressed: registerUser,
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.purple.shade50,
                  padding: EdgeInsets.symmetric(vertical: 15),
                ),
                child: const Text(
                  "Register",
                  style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                ),
              ),

              // Hiển thị thông báo lỗi
              if (errorMessage.isNotEmpty) ...[
                const SizedBox(height: 20),
                Text(
                  errorMessage,
                  style: const TextStyle(color: Colors.red, fontSize: 16),
                ),
              ],

              const SizedBox(height: 30),
              Center(
                child: GestureDetector(
                  onTap: () {
                    Navigator.pushReplacementNamed(context, '/login');
                  },
                  child: const Text(
                    'Already have an account? Login now',
                    style: TextStyle(
                      color: Colors.blueAccent,
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
