import 'package:appcl/models/ClassInstance.dart';
import 'package:appcl/models/YogaClass.dart';
import 'package:appcl/services/class_instance_service.dart';
import 'package:appcl/services/yogaclass_service.dart';
import 'package:flutter/material.dart';

import '../widgets/ClassInstanceItem.dart';
import 'Detail_ClassInstance_Screen.dart';

class SearchClassInstanceScreen extends StatefulWidget {
  @override
  _SearchClassInstanceScreenState createState() => _SearchClassInstanceScreenState();
}

class _SearchClassInstanceScreenState extends State<SearchClassInstanceScreen> {
  String? selectedDayOfWeek;
  bool allTimesSelected = false;
  TextEditingController timeController = TextEditingController();
  final ClassInstanceService _classInstanceService = ClassInstanceService();

  String? errorMessage;
  List<ClassInstance> classInstances = [];

  void _showFilterResults() async {
    if (selectedDayOfWeek == null) {
      setState(() {
        errorMessage = "Please select a day of the week.";
      });
      return;
    }

    setState(() {
      errorMessage = "";
    });

    String? day = selectedDayOfWeek;
    String? time = allTimesSelected ? null : (timeController.text.isNotEmpty ? timeController.text : null);

    try {
      List<YogaClass> filteredClasses;
      if (time != null) {
        filteredClasses = await YogaclassService().getYogaClassesByDayOrTime(day, time);
      } else {
        filteredClasses = await YogaclassService().getYogaClassesByDay(day);
      }

      if (filteredClasses.isEmpty) {
        setState(() {
          errorMessage = "No classes found for the selected day and/or time.";
        });
        return;
      }

      this.classInstances.clear();

      for (var yogaClass in filteredClasses) {
        List<ClassInstance> instances = await _classInstanceService.getClassInstancesByClassId(yogaClass.classId);
        classInstances.addAll(instances);
      }

      setState(() {
        this.classInstances = classInstances;
      });
    } catch (e) {
      setState(() {
        errorMessage = "Error: $e";
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Search"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          children: [
            Container(
              decoration: BoxDecoration(
                color: Colors.purpleAccent.withOpacity(0.05),
                borderRadius: BorderRadius.circular(16),
              ),
              child: Padding(
                padding: EdgeInsets.all(10),
                child: Column(
                  children: [
                    Row(
                      children: [
                        Expanded(
                          child: DropdownButton<String>(
                            hint: Text("Select Day of Week"),
                            value: selectedDayOfWeek,
                            onChanged: (newValue) {
                              setState(() {
                                selectedDayOfWeek = newValue;
                              });
                            },
                            items: <String>[
                              'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'
                            ].map<DropdownMenuItem<String>>((String value) {
                              return DropdownMenuItem<String>(
                                value: value,
                                child: Text(value),
                              );
                            }).toList(),
                          ),
                        ),
                        SizedBox(width: 10),
                        Expanded(
                          child: TextField(
                            controller: timeController,
                            decoration: InputDecoration(
                              labelText: "Enter Time",
                              hintText: "e.g., 08:00 AM",
                              border: OutlineInputBorder(),
                            ),
                          ),
                        ),
                      ],
                    ),
                    SizedBox(height: 5),
                    SizedBox(height: 10),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        ElevatedButton(
                          onPressed: _showFilterResults,
                          child: Text("Filter"),
                        ),
                        Row(
                          children: [
                            Checkbox(
                              value: allTimesSelected,
                              onChanged: (bool? value) {
                                setState(() {
                                  allTimesSelected = value ?? false;
                                  if (allTimesSelected) timeController.clear();
                                });
                              },
                            ),
                            Text("All Times"),
                          ],
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
            SizedBox(height: 20),
            Expanded(
              child: ListView.builder(
                itemCount: classInstances.length,
                padding: EdgeInsets.symmetric(vertical: 10),
                itemBuilder: (context, index) {
                  ClassInstance instance = classInstances[index];
                  return Column(
                    children: [
                      ClassInstanceItem(
                        instance: instance,
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => DetailClassinstanceScreen(instance: instance),
                            ),
                          );
                        },
                      ),
                      SizedBox(height: 10),
                    ],
                  );
                },
              ),
            ),
            if (errorMessage != null && errorMessage!.isNotEmpty)
              Padding(
                padding: EdgeInsets.all(8.0),
                child: Text(
                  errorMessage!,
                  style: TextStyle(color: Colors.red),
                ),
              ),
          ],
        ),
      ),
    );
  }
}
