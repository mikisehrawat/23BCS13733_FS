import { useState } from 'react';
import { Trash2, Circle } from 'lucide-react';

export default function TodoApp() {
  const [tasks, setTasks] = useState([]);
  const [inputValue, setInputValue] = useState('');
  const [filter, setFilter] = useState('all'); // 'all', 'active', 'done'

  const addTask = () => {
    if (inputValue.trim() !== '') {
      const newTask = {
        id: Date.now(),
        text: inputValue,
        completed: false
      };
      setTasks([...tasks, newTask]);
      setInputValue('');
    }
  };

  const toggleComplete = (id) => {
    setTasks(tasks.map(task =>
      task.id === id ? { ...task, completed: !task.completed } : task
    ));
  };

  const deleteTask = (id) => {
    setTasks(tasks.filter(task => task.id !== id));
  };

  const clearCompleted = () => {
    setTasks(tasks.filter(task => !task.completed));
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      addTask();
    }
  };

  const filteredTasks = tasks.filter(task => {
    if (filter === 'active') return !task.completed;
    if (filter === 'done') return task.completed;
    return true;
  });

  return (
    <div className="min-h-screen py-8 px-4" style={{ backgroundColor: '#BBDCE5' }}>
      {/* Full-width container */}
      <div className="w-full h-full px-4">
        {/* Full-size white card */}
        <div className="w-full h-full bg-white/95 backdrop-blur-lg rounded-3xl shadow-2xl p-8 border border-white/20">
          
          <div className="flex items-center justify-center gap-3 mb-6">
            <h1 className="text-5xl font-black text-gray-800">
              Todo List
            </h1>
          </div>

          <div className="flex gap-3 mb-6">
            <input
              type="text"
              value={inputValue}
              onChange={(e) => setInputValue(e.target.value)}
              onKeyPress={handleKeyPress}
              placeholder="Add a new task..."
              className="flex-1 px-5 py-4 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-400 transition-all text-gray-700 font-medium placeholder-gray-400"
            />
            <button
              onClick={addTask}
              className="text-white px-10 py-4 rounded-lg flex items-center gap-3 transition-all font-bold shadow-md hover:shadow-lg hover:scale-105 transform text-lg"
              style={{ backgroundColor: '#7CB9E8' }}
              onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#6AA8D7'}
              onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#7CB9E8'}
            >
              Add
            </button>
          </div>

          <div className="flex items-center justify-between mb-6">
            <span className="text-gray-600 font-medium">{tasks.filter(t => !t.completed).length} left</span>
            <div className="flex gap-2">
              <button
                onClick={() => setFilter('all')}
                className={`px-4 py-2 rounded-lg font-medium transition-all ${
                  filter === 'all' 
                    ? 'bg-blue-100 text-blue-600 border-2 border-blue-400' 
                    : 'bg-white text-gray-600 border-2 border-gray-300 hover:bg-gray-50'
                }`}
              >
                All
              </button>
              <button
                onClick={() => setFilter('active')}
                className={`px-4 py-2 rounded-lg font-medium transition-all ${
                  filter === 'active' 
                    ? 'bg-blue-100 text-blue-600 border-2 border-blue-400' 
                    : 'bg-white text-gray-600 border-2 border-gray-300 hover:bg-gray-50'
                }`}
              >
                Active
              </button>
              <button
                onClick={() => setFilter('done')}
                className={`px-4 py-2 rounded-lg font-medium transition-all ${
                  filter === 'done' 
                    ? 'bg-blue-100 text-blue-600 border-2 border-blue-400' 
                    : 'bg-white text-gray-600 border-2 border-gray-300 hover:bg-gray-50'
                }`}
              >
                Done
              </button>
            </div>
            <button
              onClick={clearCompleted}
              className="text-red-500 font-medium hover:text-red-700 transition-colors"
            >
              Clear completed
            </button>
          </div>

          <div className="space-y-3">
            {tasks.length === 0 ? (
              <div className="text-center py-16 text-gray-400">
                <div className="mb-4 inline-block p-4 bg-gray-100 rounded-full">
                  <Circle size={56} className="opacity-50 text-gray-400" />
                </div>
                <p className="text-xl font-semibold">No tasks yet. Add one to get started!</p>
              </div>
            ) : (
              filteredTasks.map(task => (
                <div
                  key={task.id}
                  className="flex items-center gap-4 p-4 bg-white rounded-lg hover:shadow-md transition-all duration-200 group border-2 border-gray-200"
                >
                  <input
                    type="checkbox"
                    checked={task.completed}
                    onChange={() => toggleComplete(task.id)}
                    className="w-5 h-5 rounded border-2 border-gray-300 cursor-pointer accent-blue-500"
                  />
                  
                  <span
                    className={`flex-1 text-lg font-medium ${
                      task.completed
                        ? 'line-through text-gray-400'
                        : 'text-gray-800'
                    }`}
                  >
                    {task.text}
                  </span>
                  
                  <button
                    onClick={() => deleteTask(task.id)}
                    className="flex-shrink-0 text-red-500 hover:text-red-700 transition-all focus:outline-none hover:scale-110"
                  >
                    <Trash2 size={20} />
                  </button>
                </div>
              ))
            )}
          </div>

          {tasks.length > 0 && (
            <div className="mt-8 pt-6 border-t-2 border-gray-200">
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
