/**
 * Created by pedri017 on 4/27/18.\
 partner: park1394
 */
import java.awt.*;
import java.util.*;

public class Node {

    private Node left; // null if leaf
    private Node right; // null if leaf
    private Shape shape; // non-null if leaf
    private Bounds bounds; // always set in constructor

    // Getters and setters
    // Use for unit tests!
    public Node getLeft(){ return left; }
    public Node getRight(){ return right; }
    public Shape getShape(){ return shape; }
    public Bounds getBounds(){ return bounds; }
    public void setLeft( Node l ){ left = l; }
    public void setRight( Node r ){ right = r; }
    public void setShape( Shape s ){ shape = s; }
    public void setBounds( Bounds b ){ bounds = b; }

    // TODO
    // 2) The constructor takes a stack of shapes and an (initial) splitting plane axis.
    // If there is one shape, then it stores the reference and becomes a leaf node.
    // If there are two or more shapes, it will partition the stack into seperate stacks,
    // create children, and pass the stacks to the children.
    // This is what we call top-down tree construction.
    public Node(Stack<Shape> stack, int axis){

        // Set our objects to null, so we know not to use
        // them if they are never set.
        left = null;
        right = null;
        shape = null;
        bounds = new Bounds();

        // We should never have an empty stack!
        if( stack.size() == 0 ){
            throw new RuntimeException("**Node Error: Empty stack!");
        }

        //If our stack only has one shape, we are a leaf node!
        if( stack.size() == 1) {
            shape = stack.pop();
            bounds.extend(shape.getBounds());       //Extend our bounding box to contain everything in the stack
        }

        else {
            Shape tempShape;
            Stack<Shape> tempStack = new Stack<Shape>();
            while (!stack.isEmpty()) {
                tempShape=stack.pop();
                bounds.extend(tempShape.getBounds());
                tempStack.push(tempShape);
            }

            while (!tempStack.isEmpty()) {
                tempShape=tempStack.pop();
                stack.push(tempShape);
            }

            //split the stack!
                Stack<Shape> leftStack = new Stack<Shape>();        //check if we need this defined outside of the constructor
                Stack<Shape> rightStack = new Stack<Shape>();
                int splitAxis;
                if ( axis== 1)
                    splitAxis=1;
                else
                    splitAxis=0;
                splitStack(stack, splitAxis, leftStack, rightStack);
                left=new Node(leftStack, splitAxis);
                right=new Node(rightStack, splitAxis);
        }


        // switch the axis between 1 and 0 So if our current node was split on the
        // x axis, the children will be split along the y axis. .

    } // end constructor


    //  To decide which shape goes on which stack, we'll compute
    // the center of all objects currently in the stack. Objects that are less than the median
    // go on the left stack, greater than or equal to on the right.
    public void splitStack(Stack<Shape> stack, int axis, Stack<Shape> leftStack, Stack<Shape> rightStack){
        Iterator<Shape> iterator =stack.iterator();

        if( stack.size() == 0 ){    // We should never call split stack with an empty stack.
            throw new RuntimeException("**Node Error: Empty stack!");
        }
        if(axis==0){       //Centroid is calculated by splitting the x axis
            int count=0;
            int sum=0;
            while (iterator.hasNext()) {
                count +=1;
                sum+=iterator.next().getCenter().x;
            }
            int cent=sum/count;

            while(!stack.isEmpty()){
                if (stack.peek().getCenter().x>cent){
                    rightStack.push(stack.pop());
                }
                else{
                    leftStack.push(stack.pop());
                }
            }
        }

        else if(axis==1){       //Centroid is calculated by splitting the y axis

            int count=0;
            int sum=0;
            while (iterator.hasNext()) {
                count +=1;
                sum+=iterator.next().getCenter().y;
            }

            int cent=sum/count;

            while(!stack.isEmpty()){
                if (stack.peek().getCenter().y>cent){
                    rightStack.push(stack.pop());
                }
                else{
                    leftStack.push(stack.pop());
                }
            }
        }
        // 3a) First, compute the centroid. This is the average of all vertices.        //ISNT THIS NOT ALLOWED FOR A STACK?
        // We'll use an iterator so we don't change the stack (yet).

        // 3b) Now that we know the center, we can partition the stack
        // into two seperate ones!


        // Make sure both stacks have at least one element.
        // There are two ways this error would trigger:
        // -You made a mistake in your stack splitting
        // -Two elements have the same center along a specific axis (possible, but unlikely).
        if( leftStack.empty() || rightStack.empty() ){
            throw new RuntimeException("**splitStack Error: Empty child stack after split!");
        }

    } // end split stack

    // TODO
    // 4) Traverse the tree and find the selected shape.
    // If we're a leaf, test against the shape.
    // If we're a node, test children.
    // Only one shape should be selected at a time.
    public boolean select(double x, double y, int[] counter){
        counter[0]++; // Don't remove this

        if (bounds.isOutside(x,y)) {       //if point is outside bound
            return false;
        }

        if (shape!=null){
            return shape.select(x,y);

        }

        else{
            if ( right.getBounds().exteriorDistance(x,y) < left.getBounds().exteriorDistance(x,y))
                    return right.select(x,y,counter);
            else if (right.getBounds().exteriorDistance(x,y) > left.getBounds().exteriorDistance(x,y))
                return left.select(x,y,counter);
            else
                return right.select(x,y,counter) || left.select(x,y,counter);
        }




        // 4a) If we're outside the bounds of the node, we
        // don't need to check children!

        // 4b) If we are a leaf, check the shape

        // If we aren't a leaf, we should have both
        // a left and right child.

        // 4c) Otherwise, traverse children!
        // Since we assume no overlapping shapes, return true
        // if one was found.



    } // end select

    // Returns true if it finds a closer shape, in which is sets shapeRef and currentMin.
    // currentMin is ONLY updated when a closer shape is found.
    public boolean nearest(double x, double y, double[] currentMin, Shape[] shapeRef, int[] counter){
        counter[0]++; // Don't remove this

        // 5a) Check exterior distance between point and AABB.
        // If it's larger than the current min, return false
        // (since we know we're farther away than the current min).

        // 5b) If we are a leaf, check exterior distance
        // between the point and shape. If that exterior distance
        // is less than the current min, update the shapeRef
        // and currentMin, then return true.

        // If we aren't a leaf, we should have both
        // a left and right child.
        if( right == null || left == null ){
            throw new NullPointerException();
        }

        // 5c) Otherwise, traverse children!
        // As in select, we'll try to minimize tree traversal.
        // See which node is closer and traverse that branch first.

        return false;

    } // end nearest

    // Draw the boundaries of the node and children
    public void paint(Graphics2D g){

        // Our bounds should visibly enclose everything below it on the tree.
        bounds.paint(g);

        // If we're a leaf node, draw the shape contained by this node:
        if( shape != null ){
            return;
        }

        // If we aren't a leaf, we should have both
        // a left and right child.
        if( right == null || left == null ){
            throw new NullPointerException();
        }

        left.paint(g);
        right.paint(g);

    } // end paint


} // end class Node
