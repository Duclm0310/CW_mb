import 'package:flutter/material.dart';
import '../models/ClassInstance.dart';

class ClassInstanceItem extends StatelessWidget {
  final ClassInstance instance;
  final VoidCallback onTap;

  const ClassInstanceItem({
    Key? key,
    required this.instance,
    required this.onTap,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
          color: Colors.purpleAccent.withOpacity(0.05),
        ),
        child: Column(
          children: [
            ListTile(
              contentPadding: EdgeInsets.all(16),
              title: Text(
                instance.classTitle,
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
              subtitle: Text(
                "Session Date: ${instance.formattedDate}\nUser Name: ${instance.userName}",
              ),
            ),
          ],
        ),
      ),
    );
  }
}
