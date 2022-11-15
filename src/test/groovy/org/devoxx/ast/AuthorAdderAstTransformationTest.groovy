package org.devoxx.ast

import groovy.test.GroovyAssert
import org.junit.jupiter.api.Test



class AuthorAdderAstTransformationTest {

    @Test
    void 'every class should has $AUTHOR field'() {
        GroovyAssert.assertScript('''
        class Foo {}
        assert Foo.$AUTHOR == 'kartik'
        ''')
    }

    @Test
    void 'every class with @Messenger should have message method'() {
        GroovyAssert.assertScript('''
            import org.devoxx.ast.Messenger
            
            def out = new ByteArrayOutputStream()
            System.out = new PrintStream(out)
            def content = new StringWriter()
            binding.out = new PrintWriter(content)
    
    
            @Messenger(shout = false)
            class QuietFoo {}
    
            new QuietFoo().message('hello world')
            assert out.toString() == 'hello world\\n'
            out.reset()
    
            @Messenger(shout = true)
            class LoudFoo {}
            new LoudFoo().message('hello world')
            assert out.toString() == 'HELLO WORLD\\n'
        ''')

    }

    @Test
    void 'class with method @main should have psvm'() {
        GroovyAssert.assertScript'''
        import org.devoxx.ast.Main
        
        def out = new ByteArrayOutputStream()
        System.out = new PrintStream(out)
        def content = new StringWriter()
        binding.out = new PrintWriter(content)

        class Foo {
            @Main
            def greet() {
                println 'hello world'
            }
        }

        Foo.main(new String[0])
        assert out.toString() == 'hello world\\n'
        '''
    }

    @Test
    void 'main method exist in source code'() {
        GroovyAssert.assertScript'''
        import org.devoxx.ast.Main
        
        class Foo {
            static void main(String[] args) {
                assert false, 'Too many main'
            }

            @Main
            def greet() {
                println 'hello world'
            }
        }
        '''
    }

}