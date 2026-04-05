import 'dart:convert';
import 'package:http/http.dart' as http;

/// Simple Gemini REST client.
/// Supports `gemini-2.0-flash`, `gemini-1.5-flash`, etc.
class GeminiClient {
  static const _base = 'https://generativelanguage.googleapis.com/v1beta/models';

  /// Send a single-turn message and return the text response.
  /// [history] is a list of prior turns: `[{role:'user',text:'...'},{role:'model',text:'...'}]`
  static Future<String> generate({
    required String apiKey,
    required String model,
    required String systemPrompt,
    required String userMessage,
    List<Map<String, String>> history = const [],
    int maxTokens = 256,
    double temperature = 0.7,
  }) async {
    if (apiKey.isEmpty) return 'No Gemini API key set. Add it in Settings.';

    final contents = <Map<String, dynamic>>[];

    // Prior conversation turns
    for (final turn in history) {
      contents.add({
        'role': turn['role'],
        'parts': [{'text': turn['text']}],
      });
    }
    // Current user message
    contents.add({
      'role': 'user',
      'parts': [{'text': userMessage}],
    });

    final body = {
      'system_instruction': {
        'parts': [{'text': systemPrompt}],
      },
      'contents': contents,
      'generationConfig': {
        'maxOutputTokens': maxTokens,
        'temperature': temperature,
      },
    };

    try {
      final uri = Uri.parse('$_base/$model:generateContent?key=$apiKey');
      final res = await http
          .post(uri,
              headers: {'Content-Type': 'application/json'},
              body: jsonEncode(body))
          .timeout(const Duration(seconds: 12));

      if (res.statusCode != 200) {
        final err = jsonDecode(res.body);
        final msg = (err['error']?['message'] as String?) ?? 'HTTP ${res.statusCode}';
        return 'Gemini error: $msg';
      }

      final j         = jsonDecode(res.body) as Map<String, dynamic>;
      final candidates = j['candidates'] as List?;
      if (candidates == null || candidates.isEmpty) return 'No response from Gemini.';
      final parts = (candidates[0]['content']?['parts'] as List?) ?? [];
      if (parts.isEmpty) return 'Empty response.';
      return (parts[0]['text'] as String? ?? '').trim();
    } catch (e) {
      return 'Error: ${e.toString().split('\n').first}';
    }
  }
}
