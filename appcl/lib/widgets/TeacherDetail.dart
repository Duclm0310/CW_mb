import 'package:flutter/material.dart';

import '../models/user.dart';


class Teacherdetail extends StatelessWidget {
  final User teacher;
  const Teacherdetail({super.key, required this.teacher});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Padding(
          padding: EdgeInsets.all(8.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Container(
                padding: EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(16),
                    color: Colors.purpleAccent.withOpacity(0.05)
                  ),
                child: Text(teacher.name,
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 26
                ),),
              ),
              SizedBox(height: 10,),
              Container(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text("Email: ${teacher.email}",
                    style: TextStyle(
                      fontSize: 16
                    ),),
                    Text("Role: ${teacher.role}")
                  ],
                ),
              )
            ],
          ),
      ),
    );
  }
}
