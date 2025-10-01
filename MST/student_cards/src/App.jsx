import React from 'react';

// Reusable StudentCard Component
const StudentCard = ({ name, rollNumber, course }) => {
  // Generate consistent colors based on first letter
  const colors = [
    'bg-gradient-to-br from-purple-500 to-pink-500',
    'bg-gradient-to-br from-blue-500 to-cyan-500',
    'bg-gradient-to-br from-green-500 to-emerald-500',
    'bg-gradient-to-br from-orange-500 to-red-500',
    'bg-gradient-to-br from-indigo-500 to-purple-500',
    'bg-gradient-to-br from-teal-500 to-green-500',
  ];
  
  const colorIndex = name.charCodeAt(0) % colors.length;
  const avatarColor = colors[colorIndex];
  
  return (
    <div className="bg-white rounded-xl shadow-lg p-6 hover:shadow-2xl hover:-translate-y-1 transition-all duration-300 border-2 border-gray-100">
      <div className="flex items-center mb-5">
        <div className={`w-16 h-16 ${avatarColor} rounded-full flex items-center justify-center text-white font-bold text-2xl shadow-md`}>
          {name.charAt(0)}
        </div>
        <div className="ml-4 flex-1">
          <h3 className="text-xl font-bold text-gray-800 mb-1">{name}</h3>
          <span className="inline-block bg-blue-100 text-blue-700 text-xs font-semibold px-3 py-1 rounded-full">
            Student
          </span>
        </div>
      </div>
      
      <div className="space-y-3 pt-4 border-t-2 border-gray-100">
        <div className="flex items-start">
          <div className="flex-shrink-0 w-8 h-8 bg-blue-50 rounded-lg flex items-center justify-center mr-3">
            <span className="text-blue-600 font-bold text-sm">#</span>
          </div>
          <div>
            <p className="text-xs text-gray-500 font-medium uppercase tracking-wide">Roll Number</p>
            <p className="text-gray-800 font-semibold">{rollNumber}</p>
          </div>
        </div>
        
        <div className="flex items-start">
          <div className="flex-shrink-0 w-8 h-8 bg-purple-50 rounded-lg flex items-center justify-center mr-3">
            <span className="text-purple-600 font-bold text-sm">📚</span>
          </div>
          <div>
            <p className="text-xs text-gray-500 font-medium uppercase tracking-wide">Course</p>
            <p className="text-gray-800 font-semibold">{course}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

// Main App Component
export default function App() {
  // Array of student objects
  const students = [
    {
      id: 1,
      name: "Arjun Singh",
      rollNumber: "23BCS10245",
      course: "Computer Science"
    },
    {
      id: 2,
      name: "Priya Sharma",
      rollNumber: "23BEC14523",
      course: "Electronics & Communication"
    },
    {
      id: 3,
      name: "Rahul Kumar",
      rollNumber: "23BME12345",
      course: "Mechanical Engineering"
    },
    {
      id: 4,
      name: "Sneha Patel",
      rollNumber: "23BIT12045",
      course: "Information Technology"
    },
    {
      id: 5,
      name: "Vikram Reddy",
      rollNumber: "EE2021056",
      course: "Electrical Engineering"
    },
    {
      id: 6,
      name: "Ananya Desai",
      rollNumber: "23BCS12047",
      course: "Computer Science"
    }
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 via-purple-50 to-pink-50 py-12 px-4">
      <div className="max-w-7xl mx-auto">
        <div className="text-center mb-12">
          <h1 className="text-5xl font-extrabold text-gray-900 mb-3">
            Student Directory
          </h1>
          <p className="text-gray-600 text-lg">
            Browse our current student roster
          </p>
          <div className="mt-4 inline-block bg-white px-6 py-2 rounded-full shadow-md">
            <span className="text-gray-700 font-semibold">{students.length} Students Enrolled</span>
          </div>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {students.map((student) => (
            <StudentCard
              key={student.id}
              name={student.name}
              rollNumber={student.rollNumber}
              course={student.course}
            />
          ))}
        </div>
      </div>
    </div>
  );
}