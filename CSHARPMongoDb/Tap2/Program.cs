using System;
using System.IO;
using System.Linq;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Driver;

class Program
{
    static IMongoDatabase database;

    static void Main()
    {
        // Устанавливаем соединение с MongoDB
        var client = new MongoClient("mongodb://localhost:27017");
        database = client.GetDatabase("myDatabase");

        Console.WriteLine("1. Сохранить файл");
        Console.WriteLine("2. Вывести файлы");
        Console.Write("Выберите действие (1 или 2): ");

        if (int.TryParse(Console.ReadLine(), out int choice))
        {
            switch (choice)
            {
                case 1:
                    SaveFile();
                    break;
                case 2:
                    DisplayFiles();
                    break;
                default:
                    Console.WriteLine("Некорректный выбор.");
                    break;
            }
        }
        else
        {
            Console.WriteLine("Некорректный ввод.");
        }
    }

    static void SaveFile()
    {
        Console.Write("Введите путь к файлу для сохранения: ");
        string filePath = Console.ReadLine();

        if (File.Exists(filePath))
        {
            string extension = Path.GetExtension(filePath).ToLower();

            if (extension == ".json" || extension == ".xml" || extension == ".csv")
            {
                var document = new
                {
                    FileName = Path.GetFileName(filePath),
                    Content = File.ReadAllText(filePath),
                    FileType = extension
                };

                var collection = database.GetCollection<BsonDocument>("documents");
                collection.InsertOne(document.ToBsonDocument());

                Console.WriteLine("Файл успешно сохранен в хранилище.");
            }
            else
            {
                Console.WriteLine("Неподдерживаемый формат файла. Поддерживаемые форматы: *.json, *.xml, *.csv");
            }
        }
        else
        {
            Console.WriteLine("Файл не найден.");
        }
    }

    static void DisplayFiles()
    {
        var collection = database.GetCollection<BsonDocument>("documents");
        var documents = collection.Find(new BsonDocument()).ToList();

        if (documents.Any())
        {
            foreach (var document in documents)
            {
                Console.WriteLine($"Имя файла: {document.GetValue("FileName")}, Тип файла: {document.GetValue("FileType")}");
                Console.WriteLine("Содержимое файла:");
                Console.WriteLine(document.GetValue("Content"));
                Console.WriteLine("---------------");
            }
        }
        else
        {
            Console.WriteLine("Хранилище пусто. Нет сохраненных файлов.");
        }
    }
}
